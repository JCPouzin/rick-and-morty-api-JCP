package org.mathieu.cleanrmapi.data.repositories

import org.mathieu.cleanrmapi.data.local.EpisodeDAO
import org.mathieu.cleanrmapi.data.local.objects.CharacterEpisodeObject
import org.mathieu.cleanrmapi.data.local.objects.toModel
import org.mathieu.cleanrmapi.data.local.objects.toRoomObject
import org.mathieu.cleanrmapi.data.remote.CharacterApi
import org.mathieu.cleanrmapi.data.remote.EpisodeApi
import org.mathieu.cleanrmapi.domain.models.episode.Episode
import org.mathieu.cleanrmapi.domain.repositories.EpisodeRepository

class EpisodeRepositoryImpl(
    private val episodeApi: EpisodeApi,
    private val episodeLocal: EpisodeDAO,
    private val characterApi: CharacterApi
) : EpisodeRepository {

    override suspend fun getEpisodes(characterId: Int): List<Episode> {
        // Tente d'abord de récupérer les épisodes depuis la source locale.
        val episodesLocal = episodeLocal.getEpisodesForCharacter(characterId)
        if (episodesLocal.isNotEmpty()) {
            return episodesLocal.map { it.toModel() }
        }

        // Si aucun épisode local trouvé, récupère les IDs des épisodes depuis l'API du personnage.
        val episodesToLoad = characterApi.getCharacter(characterId)?.episode?.mapNotNull {
            it.substringAfterLast("/").toIntOrNull()
        } ?: throw Exception("Character not found.")

        // Récupère les épisodes depuis l'API des épisodes en utilisant les IDs trouvés.
        val episodes = episodeApi.getEpisodes(episodesToLoad)
            .map { it.toRoomObject() }

        // Sauvegarde les épisodes récupérés dans la base de données locale.
        episodeLocal.insert(episodes)

        // Crée une liaison entre le personnage et ses épisodes dans la base de données.
        val links = episodes.map {
            CharacterEpisodeObject(
                characterId = characterId,
                episodeId = it.id
            )
        }
        episodeLocal.createLink(links)

        // Retourne les épisodes sous forme de modèle de domaine.
        return episodes.map { it.toModel() }
    }
}

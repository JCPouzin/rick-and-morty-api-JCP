package org.mathieu.cleanrmapi.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.mathieu.cleanrmapi.data.local.CharacterDAO
import org.mathieu.cleanrmapi.data.local.objects.CharacterObject
import org.mathieu.cleanrmapi.data.local.objects.toModel
import org.mathieu.cleanrmapi.data.local.objects.toRealmObject
import org.mathieu.cleanrmapi.data.remote.CharacterApi
import org.mathieu.cleanrmapi.data.remote.responses.CharacterResponse
import org.mathieu.cleanrmapi.domain.models.character.Character
import org.mathieu.cleanrmapi.domain.repositories.CharacterRepository
import org.mathieu.cleanrmapi.domain.repositories.EpisodeRepository

// Préférences pour la pagination des personnages.
private const val CHARACTER_PREFS = "character_repository_preferences"
private val nextPage = intPreferencesKey("next_characters_page_to_load")

// Extension pour faciliter l'accès au datastore spécifique des personnages.
private val Context.dataStore by preferencesDataStore(name = CHARACTER_PREFS)

internal class CharacterRepositoryImpl(
    private val context: Context,
    private val characterApi: CharacterApi,
    private val characterLocal: CharacterDAO,
    private val episodeRepository: EpisodeRepository
) : CharacterRepository {

    // Obtient la liste des personnages depuis le stockage local et charge le prochain lot si nécessaire.
    override suspend fun getCharacters(): Flow<List<Character>> =
        characterLocal
            .getCharacters()
            .mapElement(transform = CharacterObject::toModel)
            .also { if (it.first().isEmpty()) fetchNext() }

    // Charge le prochain lot de personnages depuis l'API et les enregistre localement.
    private suspend fun fetchNext() {
        val page = context.dataStore.data.map { prefs -> prefs[nextPage] }.first()

        if (page != -1) {
            val response = characterApi.getCharacters(page)
            val nextPageToLoad = response.info.next?.split("?page=")?.last()?.toInt() ?: -1
            context.dataStore.edit { prefs -> prefs[nextPage] = nextPageToLoad }

            val objects = response.results.map(CharacterResponse::toRealmObject)
            characterLocal.saveCharacters(objects)
        }
    }

    // Charge plus de personnages en appelant fetchNext.
    override suspend fun loadMore() = fetchNext()

    // Récupère un personnage par son ID, en le chargeant depuis l'API si nécessaire.
    override suspend fun getCharacter(id: Int): Character {
        var character = characterLocal.getCharacter(id)
            ?: characterApi.getCharacter(id = id)?.toRealmObject()?.also {
                characterLocal.insert(it)
            } ?: throw Exception("Character not found.")

        // Récupère les épisodes associés au personnage.
        val episodes = episodeRepository.getEpisodes(character.id)
        return character.toModel(episodes)
    }
}

// Fonction d'aide pour exécuter un bloc de code et retourner null en cas d'exception.
fun <T> tryOrNull(block: () -> T) = try {
    block()
} catch (_: Exception) {
    null
}

// Transforme chaque élément d'un Flow<List<T>> en utilisant la fonction transform fournie.
inline fun <T, R> Flow<List<T>>.mapElement(crossinline transform: suspend (value: T) -> R): Flow<List<R>> =
    this.map { list -> list.map { element -> transform(element) } }

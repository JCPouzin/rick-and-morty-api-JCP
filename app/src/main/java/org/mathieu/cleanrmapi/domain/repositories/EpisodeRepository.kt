package org.mathieu.cleanrmapi.domain.repositories

import org.mathieu.cleanrmapi.domain.models.episode.Episode

interface EpisodeRepository {

    suspend fun getEpisodes(characterId : Int): List<Episode>
}
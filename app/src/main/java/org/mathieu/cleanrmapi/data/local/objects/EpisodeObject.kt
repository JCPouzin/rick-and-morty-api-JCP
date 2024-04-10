package org.mathieu.cleanrmapi.data.local.objects

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.mathieu.cleanrmapi.data.local.RMDatabase
import org.mathieu.cleanrmapi.data.remote.responses.EpisodeResponse
import org.mathieu.cleanrmapi.domain.models.episode.Episode

@Entity(tableName = RMDatabase.EPISODE_TABLE)
class EpisodeObject(
    @PrimaryKey
    val id: Int,
    val name : String,
    val airDate : String,
    val episode : String,
    val url : String,
)

internal fun EpisodeResponse.toRoomObject() = EpisodeObject(
    id = id,
    name = name,
    airDate = air_date,
    episode = episode,
    url = url
)

internal fun EpisodeObject.toModel() = Episode(
    id = id,
    name = name,
    airDate = airDate,
    episode = episode,
    url = url
)
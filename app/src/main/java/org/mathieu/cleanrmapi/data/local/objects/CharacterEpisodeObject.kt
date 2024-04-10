package org.mathieu.cleanrmapi.data.local.objects

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.mathieu.cleanrmapi.data.local.RMDatabase

@Entity(
    tableName = RMDatabase.CHARACTER_EPISODE_TABLE,
    primaryKeys = ["characterId", "episodeId"],
    foreignKeys = [
        ForeignKey(
            entity = CharacterObject::class,
            parentColumns = ["id"],
            childColumns = ["characterId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EpisodeObject::class,
            parentColumns = ["id"],
            childColumns = ["episodeId"],
            onDelete = ForeignKey.CASCADE
        )

    ])
class CharacterEpisodeObject(
    val characterId: Int,
    val episodeId: Int,
)
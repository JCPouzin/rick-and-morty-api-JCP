package org.mathieu.cleanrmapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.mathieu.cleanrmapi.data.local.objects.CharacterObject
import org.mathieu.cleanrmapi.data.local.objects.CharacterEpisodeObject
import org.mathieu.cleanrmapi.data.local.objects.EpisodeObject

// Annotation Database pour définir les entités.
@Database(
    entities = [
        CharacterObject::class,
        EpisodeObject::class,
        CharacterEpisodeObject::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RMDatabase : RoomDatabase() {
    abstract fun characterDAO(): CharacterDAO
    abstract fun episodeDAO(): EpisodeDAO

    // Bloc pour définir des constantes utilisées dans la base de données.
    companion object {
        // Noms des tables pour un accès et des requêtes facilités.
        const val CHARACTER_TABLE = "character_table"
        const val EPISODE_TABLE = "episode_table"
        const val CHARACTER_EPISODE_TABLE = "character_episode_table"
    }
}

package org.mathieu.cleanrmapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.mathieu.cleanrmapi.data.local.objects.CharacterEpisodeObject
import org.mathieu.cleanrmapi.data.local.objects.EpisodeObject

// Définition de l'interface DAO pour les opérations sur les épisodes dans la base de données.
@Dao
interface EpisodeDAO {

    // Insertion d'une liste d'épisodes dans la base de données.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episodes: List<EpisodeObject>)

    // Création de liens entre des personnages et des épisodes dans la table.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createLink(link: List<CharacterEpisodeObject>)

    // Récupération de tous les épisodes liés à un personnage spécifique.
    @Query("SELECT * FROM ${RMDatabase.EPISODE_TABLE} WHERE id IN (SELECT episodeId FROM ${RMDatabase.CHARACTER_EPISODE_TABLE} WHERE characterId = :characterId)")
    suspend fun getEpisodesForCharacter(characterId: Int): List<EpisodeObject>
}

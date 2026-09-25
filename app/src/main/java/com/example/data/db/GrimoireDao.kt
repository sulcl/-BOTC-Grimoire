package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GrimoireDao {
    @Query("SELECT * FROM games ORDER BY updatedAt DESC")
    fun getAllGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :id LIMIT 1")
    fun getGameById(id: Long): Flow<GameEntity?>

    @Query("SELECT * FROM games ORDER BY updatedAt DESC LIMIT 1")
    fun getLatestGame(): Flow<GameEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity): Long

    @Update
    suspend fun updateGame(game: GameEntity)

    @Query("DELETE FROM games WHERE id = :id")
    suspend fun deleteGame(id: Long)
}

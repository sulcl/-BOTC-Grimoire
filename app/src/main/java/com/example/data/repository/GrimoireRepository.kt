package com.example.data.repository

import com.example.data.db.GameEntity
import com.example.data.db.GrimoireDao
import com.example.data.model.Alignment
import com.example.data.model.CharactersData
import com.example.data.model.GameLogEntry
import com.example.data.model.GamePhase
import com.example.data.model.NominationRecord
import com.example.data.model.PlayerSeat
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GrimoireRepository(private val grimoireDao: GrimoireDao) {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val seatsListType = Types.newParameterizedType(List::class.java, PlayerSeat::class.java)
    private val nominationsListType = Types.newParameterizedType(List::class.java, NominationRecord::class.java)
    private val logsListType = Types.newParameterizedType(List::class.java, GameLogEntry::class.java)

    private val seatsAdapter = moshi.adapter<List<PlayerSeat>>(seatsListType)
    private val nominationsAdapter = moshi.adapter<List<NominationRecord>>(nominationsListType)
    private val logsAdapter = moshi.adapter<List<GameLogEntry>>(logsListType)

    val allGames: Flow<List<GameEntity>> = grimoireDao.getAllGames()
    val latestGame: Flow<GameEntity?> = grimoireDao.getLatestGame()

    fun getGameById(id: Long): Flow<GameEntity?> = grimoireDao.getGameById(id)

    suspend fun saveGame(
        id: Long = 0,
        title: String,
        scriptName: String,
        playerCount: Int,
        phase: GamePhase,
        dayNumber: Int,
        currentStepIndex: Int,
        seats: List<PlayerSeat>,
        nominations: List<NominationRecord>,
        gameLogs: List<GameLogEntry>,
        activeBlockNomineeSeat: Int?,
        activeBlockVotes: Int,
        winner: String?,
        isNightRunning: Boolean
    ): Long {
        val seatsJson = seatsAdapter.toJson(seats)
        val nominationsJson = nominationsAdapter.toJson(nominations)
        val logsJson = logsAdapter.toJson(gameLogs)

        val entity = GameEntity(
            id = id,
            title = title,
            scriptName = scriptName,
            playerCount = playerCount,
            phase = phase.name,
            dayNumber = dayNumber,
            currentStepIndex = currentStepIndex,
            seatsJson = seatsJson,
            nominationsJson = nominationsJson,
            gameLogsJson = logsJson,
            activeBlockNomineeSeat = activeBlockNomineeSeat,
            activeBlockVotes = activeBlockVotes,
            winner = winner,
            isNightRunning = isNightRunning,
            updatedAt = System.currentTimeMillis()
        )

        return if (id == 0L) {
            grimoireDao.insertGame(entity)
        } else {
            grimoireDao.updateGame(entity)
            id
        }
    }

    suspend fun deleteGame(id: Long) {
        grimoireDao.deleteGame(id)
    }

    fun parseSeats(json: String?): List<PlayerSeat> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            seatsAdapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun parseNominations(json: String?): List<NominationRecord> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            nominationsAdapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun parseLogs(json: String?): List<GameLogEntry> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            logsAdapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun createInitialSeats(count: Int, script: String = CharactersData.SCRIPT_TROUBLE_BREWING): List<PlayerSeat> {
        val list = mutableListOf<PlayerSeat>()
        // Default roles for trouble brewing starter 7 players if count == 7 or general count
        val defaultTbRoles = listOf(
            "washerwoman", "investigator", "chef", "empath", "monk", "poisoner", "imp",
            "butler", "virgin", "slayer", "soldier", "scarletwoman", "baron", "saint", "mayor"
        )
        for (i in 1..count) {
            val roleId = defaultTbRoles.getOrNull(i - 1)
            val char = CharactersData.findCharacter(roleId)
            val alignment = char?.defaultAlignment ?: Alignment.GOOD
            list.add(
                PlayerSeat(
                    seatNumber = i,
                    playerName = "Player $i",
                    characterId = roleId,
                    alignment = alignment,
                    isAlive = true,
                    hasGhostVote = true,
                    reminders = emptyList()
                )
            )
        }
        return list
    }
}

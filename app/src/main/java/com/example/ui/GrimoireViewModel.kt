package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.GameEntity
import com.example.data.db.GrimoireDatabase
import com.example.data.model.Alignment as BotcAlignment
import com.example.data.model.CharactersData
import com.example.data.model.GameLogEntry
import com.example.data.model.GamePhase
import com.example.data.model.NominationRecord
import com.example.data.model.PlayerSeat
import com.example.data.repository.GrimoireRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class ScreenDestination {
    GRIMOIRE,
    NIGHT_RUNNER,
    DAY_COUNCIL,
    ALMANAC
}

data class BotcGameState(
    val id: Long = 0,
    val title: String = "Trouble Brewing",
    val scriptName: String = CharactersData.SCRIPT_TROUBLE_BREWING,
    val playerCount: Int = 8,
    val phase: GamePhase = GamePhase.FIRST_NIGHT,
    val dayNumber: Int = 1,
    val currentStepIndex: Int = 0,
    val seats: List<PlayerSeat> = emptyList(),
    val nominations: List<NominationRecord> = emptyList(),
    val gameLogs: List<GameLogEntry> = emptyList(),
    val activeBlockNomineeSeat: Int? = null,
    val activeBlockVotes: Int = 0,
    val winner: String? = null,
    val isNightRunning: Boolean = false,
    val currentDestination: ScreenDestination = ScreenDestination.GRIMOIRE
)

class GrimoireViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GrimoireRepository

    private val _gameState = MutableStateFlow(BotcGameState())
    val gameState: StateFlow<BotcGameState> = _gameState.asStateFlow()

    val allSavedGames: StateFlow<List<GameEntity>>

    init {
        val database = GrimoireDatabase.getDatabase(application)
        repository = GrimoireRepository(database.grimoireDao())
        allSavedGames = repository.allGames.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        // Load latest saved game or create standard starter game
        viewModelScope.launch {
            val latest = repository.latestGame.firstOrNull()
            if (latest != null) {
                loadFromEntity(latest)
            } else {
                createNewGame(
                    title = "Trouble Brewing",
                    script = CharactersData.SCRIPT_TROUBLE_BREWING,
                    playerCount = 8
                )
            }
        }
    }

    fun navigateTo(destination: ScreenDestination) {
        _gameState.value = _gameState.value.copy(currentDestination = destination)
    }

    fun createNewGame(title: String, script: String, playerCount: Int) {
        viewModelScope.launch {
            val defaultSeats = repository.createInitialSeats(playerCount, script)
            val newState = BotcGameState(
                id = 0,
                title = title,
                scriptName = script,
                playerCount = playerCount,
                phase = GamePhase.FIRST_NIGHT,
                dayNumber = 1,
                currentStepIndex = 0,
                seats = defaultSeats,
                nominations = emptyList(),
                gameLogs = listOf(
                    GameLogEntry(
                        id = UUID.randomUUID().toString(),
                        phase = "SETUP",
                        text = "Grimoire created for $title with $playerCount players."
                    )
                ),
                activeBlockNomineeSeat = null,
                activeBlockVotes = 0,
                currentDestination = ScreenDestination.GRIMOIRE
            )
            _gameState.value = newState
            persistCurrentState()
        }
    }

    fun loadGame(id: Long) {
        viewModelScope.launch {
            val entity = repository.getGameById(id).firstOrNull()
            if (entity != null) {
                loadFromEntity(entity)
            }
        }
    }

    fun deleteGame(id: Long) {
        viewModelScope.launch {
            repository.deleteGame(id)
        }
    }

    private fun loadFromEntity(entity: GameEntity) {
        val seats = repository.parseSeats(entity.seatsJson)
        val nominations = repository.parseNominations(entity.nominationsJson)
        val logs = repository.parseLogs(entity.gameLogsJson)
        val phaseEnum = try {
            GamePhase.valueOf(entity.phase)
        } catch (_: Exception) {
            GamePhase.FIRST_NIGHT
        }

        _gameState.value = BotcGameState(
            id = entity.id,
            title = entity.title,
            scriptName = entity.scriptName,
            playerCount = entity.playerCount,
            phase = phaseEnum,
            dayNumber = entity.dayNumber,
            currentStepIndex = entity.currentStepIndex,
            seats = seats,
            nominations = nominations,
            gameLogs = logs,
            activeBlockNomineeSeat = entity.activeBlockNomineeSeat,
            activeBlockVotes = entity.activeBlockVotes,
            winner = entity.winner,
            isNightRunning = entity.isNightRunning,
            currentDestination = ScreenDestination.GRIMOIRE
        )
    }

    fun updateSeat(updatedSeat: PlayerSeat) {
        val current = _gameState.value
        val updatedSeats = current.seats.map {
            if (it.seatNumber == updatedSeat.seatNumber) updatedSeat else it
        }
        _gameState.value = current.copy(seats = updatedSeats)
        persistCurrentState()
    }

    fun recordNomination(record: NominationRecord) {
        val current = _gameState.value
        val updatedNominations = current.nominations + record

        // If dead players voted, update their ghost vote status
        val updatedSeats = current.seats.map { seat ->
            if (record.ghostVotesUsedInThisNomination.contains(seat.seatNumber)) {
                seat.copy(hasGhostVote = false)
            } else {
                seat
            }
        }

        // Check if nominee goes on the block:
        // A nominee goes on the block if their votes >= majorityThreshold AND > previous block votes
        val aliveCount = updatedSeats.count { it.isAlive }
        val majorityThreshold = (aliveCount / 2) + 1
        val votesCount = record.votes.size

        var newBlockSeat = current.activeBlockNomineeSeat
        var newBlockVotes = current.activeBlockVotes

        if (votesCount >= majorityThreshold && votesCount > newBlockVotes) {
            newBlockSeat = record.nomineeSeat
            newBlockVotes = votesCount
        }

        val nominator = updatedSeats.find { it.seatNumber == record.nominatorSeat }
        val nominee = updatedSeats.find { it.seatNumber == record.nomineeSeat }

        val newLog = GameLogEntry(
            id = UUID.randomUUID().toString(),
            phase = "Day ${current.dayNumber}",
            text = "${nominator?.playerName ?: "Seat ${record.nominatorSeat}"} nominated ${nominee?.playerName ?: "Seat ${record.nomineeSeat}"} ($votesCount votes)."
        )

        _gameState.value = current.copy(
            seats = updatedSeats,
            nominations = updatedNominations,
            activeBlockNomineeSeat = newBlockSeat,
            activeBlockVotes = newBlockVotes,
            gameLogs = current.gameLogs + newLog
        )
        persistCurrentState()
    }

    fun executePlayer(seatNumber: Int) {
        val current = _gameState.value
        val executed = current.seats.find { it.seatNumber == seatNumber }
        val char = CharactersData.findCharacter(executed?.characterId)

        val updatedSeats = current.seats.map { seat ->
            if (seat.seatNumber == seatNumber) {
                seat.copy(
                    isAlive = false,
                    hasGhostVote = true, // Dead player gains 1 ghost vote token
                    reminders = seat.reminders + "Executed"
                )
            } else {
                seat
            }
        }

        val log = GameLogEntry(
            id = UUID.randomUUID().toString(),
            phase = "Day ${current.dayNumber}",
            text = "EXECUTED: ${executed?.playerName ?: "Seat $seatNumber"} (${char?.name ?: "Unknown"})."
        )

        _gameState.value = current.copy(
            seats = updatedSeats,
            activeBlockNomineeSeat = null,
            activeBlockVotes = 0,
            gameLogs = current.gameLogs + log
        )
        persistCurrentState()
    }

    fun passExecution() {
        val current = _gameState.value
        _gameState.value = current.copy(
            activeBlockNomineeSeat = null,
            activeBlockVotes = 0
        )
        persistCurrentState()
    }

    fun advancePhase() {
        val current = _gameState.value
        when (current.phase) {
            GamePhase.SETUP -> {
                _gameState.value = current.copy(
                    phase = GamePhase.FIRST_NIGHT,
                    currentDestination = ScreenDestination.NIGHT_RUNNER
                )
            }
            GamePhase.FIRST_NIGHT -> {
                _gameState.value = current.copy(
                    phase = GamePhase.DAY,
                    dayNumber = 1,
                    nominations = emptyList(),
                    activeBlockNomineeSeat = null,
                    activeBlockVotes = 0,
                    currentDestination = ScreenDestination.GRIMOIRE
                )
            }
            GamePhase.NIGHT -> {
                // Clear temporary night reminders (like protected / false info if needed)
                val cleanedSeats = current.seats.map { seat ->
                    val filteredReminders = seat.reminders.filter { it != "Protected" }
                    seat.copy(reminders = filteredReminders)
                }
                _gameState.value = current.copy(
                    seats = cleanedSeats,
                    phase = GamePhase.DAY,
                    nominations = emptyList(),
                    activeBlockNomineeSeat = null,
                    activeBlockVotes = 0,
                    currentDestination = ScreenDestination.GRIMOIRE
                )
            }
            GamePhase.DAY -> {
                _gameState.value = current.copy(
                    phase = GamePhase.NIGHT,
                    dayNumber = current.dayNumber + 1,
                    currentDestination = ScreenDestination.NIGHT_RUNNER
                )
            }
        }
        persistCurrentState()
    }

    fun applySeatNightAction(seatNumber: Int, actionType: String) {
        val current = _gameState.value
        val updatedSeats = current.seats.map { seat ->
            if (seat.seatNumber == seatNumber) {
                when (actionType) {
                    "POISON" -> seat.copy(reminders = (seat.reminders + "Poisoned").distinct())
                    "PROTECT" -> seat.copy(reminders = (seat.reminders + "Protected").distinct())
                    "KILL" -> seat.copy(isAlive = false, reminders = (seat.reminders + "Dead").distinct())
                    else -> seat
                }
            } else {
                seat
            }
        }
        _gameState.value = current.copy(seats = updatedSeats)
        persistCurrentState()
    }

    private fun persistCurrentState() {
        val state = _gameState.value
        viewModelScope.launch {
            val savedId = repository.saveGame(
                id = state.id,
                title = state.title,
                scriptName = state.scriptName,
                playerCount = state.playerCount,
                phase = state.phase,
                dayNumber = state.dayNumber,
                currentStepIndex = state.currentStepIndex,
                seats = state.seats,
                nominations = state.nominations,
                gameLogs = state.gameLogs,
                activeBlockNomineeSeat = state.activeBlockNomineeSeat,
                activeBlockVotes = state.activeBlockVotes,
                winner = state.winner,
                isNightRunning = state.isNightRunning
            )
            if (state.id == 0L && savedId > 0) {
                _gameState.value = state.copy(id = savedId)
            }
        }
    }
}

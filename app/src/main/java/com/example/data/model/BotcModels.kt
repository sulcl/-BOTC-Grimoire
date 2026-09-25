package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class CharacterType(val label: String, val color: Color) {
    TOWNSFOLK("Townsfolk", Color(0xFF1E88E5)), // Royal Blue
    OUTSIDER("Outsider", Color(0xFF00ACC1)),   // Teal / Cyan
    MINION("Minion", Color(0xFFFB8C00)),       // Tawny Rust / Orange
    DEMON("Demon", Color(0xFFE53935)),         // Blood Crimson Red
    TRAVELER("Traveler", Color(0xFF8E24AA)),   // Amethyst Violet
    FABLED("Fabled", Color(0xFFFDD835))        // Antique Gold
}

enum class Alignment(val label: String, val color: Color) {
    GOOD("Good", Color(0xFF42A5F5)),
    EVIL("Evil", Color(0xFFEF5350))
}

data class BotcCharacter(
    val id: String,
    val name: String,
    val type: CharacterType,
    val edition: String,
    val ability: String,
    val firstNightOrder: Int? = null,
    val otherNightsOrder: Int? = null,
    val firstNightReminder: String? = null,
    val otherNightsReminder: String? = null,
    val defaultReminders: List<String> = emptyList(),
    val setupRule: String? = null
) {
    val officialIconUrl: String
        get() = "https://raw.githubusercontent.com/bra1n/townsquare/develop/src/assets/icons/$id.png"

    val defaultAlignment: Alignment
        get() = when (type) {
            CharacterType.MINION, CharacterType.DEMON -> Alignment.EVIL
            else -> Alignment.GOOD
        }
}

data class PlayerSeat(
    val seatNumber: Int,
    val playerName: String = "Player $seatNumber",
    val characterId: String? = null,
    val perceivedCharacterId: String? = null, // e.g. Drunk thinks they are Monk, Lunatic thinks they are Imp
    val alignment: Alignment = Alignment.GOOD,
    val isAlive: Boolean = true,
    val hasGhostVote: Boolean = true,
    val reminders: List<String> = emptyList(),
    val notes: String = ""
)

enum class GamePhase(val label: String) {
    SETUP("Setup"),
    FIRST_NIGHT("First Night"),
    DAY("Day"),
    NIGHT("Night")
}

data class NightStep(
    val stepId: String,
    val characterId: String?,
    val title: String,
    val reminderText: String,
    val wakesIfRoleInPlayOnly: Boolean = true,
    val isDone: Boolean = false
)

data class NominationRecord(
    val id: String,
    val dayNumber: Int,
    val nominatorSeat: Int,
    val nomineeSeat: Int,
    val votes: List<Int>, // List of seat numbers that voted
    val ghostVotesUsedInThisNomination: List<Int> = emptyList(),
    val passedMajority: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class GameLogEntry(
    val id: String,
    val timestamp: Long = System.currentTimeMillis(),
    val phase: String,
    val text: String
)

data class ScriptDistribution(
    val playerCount: Int,
    val townsfolk: Int,
    val outsiders: Int,
    val minions: Int,
    val demons: Int
) {
    companion object {
        fun forPlayerCount(count: Int): ScriptDistribution {
            val clamped = count.coerceIn(5, 15)
            return when (clamped) {
                5 -> ScriptDistribution(5, 3, 0, 1, 1)
                6 -> ScriptDistribution(6, 3, 1, 1, 1)
                7 -> ScriptDistribution(7, 5, 0, 1, 1)
                8 -> ScriptDistribution(8, 5, 1, 1, 1)
                9 -> ScriptDistribution(9, 5, 2, 1, 1)
                10 -> ScriptDistribution(10, 7, 0, 2, 1)
                11 -> ScriptDistribution(11, 7, 1, 2, 1)
                12 -> ScriptDistribution(12, 7, 2, 2, 1)
                13 -> ScriptDistribution(13, 9, 0, 3, 1)
                14 -> ScriptDistribution(14, 9, 1, 3, 1)
                else -> ScriptDistribution(15, 9, 2, 3, 1)
            }
        }
    }
}

package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val scriptName: String,
    val playerCount: Int,
    val phase: String,
    val dayNumber: Int,
    val currentStepIndex: Int = 0,
    val seatsJson: String,
    val nominationsJson: String = "[]",
    val gameLogsJson: String = "[]",
    val activeBlockNomineeSeat: Int? = null,
    val activeBlockVotes: Int = 0,
    val winner: String? = null,
    val isNightRunning: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)

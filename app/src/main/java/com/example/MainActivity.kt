package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.GrimoireViewModel
import com.example.ui.ScreenDestination
import com.example.ui.screens.AlmanacScreen
import com.example.ui.screens.DayPhaseScreen
import com.example.ui.screens.GrimoireScreen
import com.example.ui.screens.NewGameDialog
import com.example.ui.screens.NightRunnerScreen
import com.example.ui.screens.SavedGamesDialog
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                BotcStorytellerApp()
            }
        }
    }
}

@Composable
fun BotcStorytellerApp(
    viewModel: GrimoireViewModel = viewModel()
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    val savedGames by viewModel.allSavedGames.collectAsStateWithLifecycle()

    var showNewGameDialog by remember { mutableStateOf(false) }
    var showSavedGamesDialog by remember { mutableStateOf(false) }

    when (gameState.currentDestination) {
        ScreenDestination.GRIMOIRE -> {
            GrimoireScreen(
                gameTitle = gameState.title,
                scriptName = gameState.scriptName,
                phase = gameState.phase,
                dayNumber = gameState.dayNumber,
                seats = gameState.seats,
                activeBlockNomineeSeat = gameState.activeBlockNomineeSeat,
                activeBlockVotes = gameState.activeBlockVotes,
                onUpdateSeat = { updated -> viewModel.updateSeat(updated) },
                onNavigateToNightRunner = { viewModel.navigateTo(ScreenDestination.NIGHT_RUNNER) },
                onNavigateToDayCouncil = { viewModel.navigateTo(ScreenDestination.DAY_COUNCIL) },
                onNavigateToAlmanac = { viewModel.navigateTo(ScreenDestination.ALMANAC) },
                onNewGameClick = { showNewGameDialog = true },
                onSavedGamesClick = { showSavedGamesDialog = true },
                onAdvancePhase = { viewModel.advancePhase() },
                modifier = Modifier.fillMaxSize()
            )
        }

        ScreenDestination.NIGHT_RUNNER -> {
            BackHandler {
                viewModel.navigateTo(ScreenDestination.GRIMOIRE)
            }
            NightRunnerScreen(
                phase = gameState.phase,
                dayNumber = gameState.dayNumber,
                seats = gameState.seats,
                onCompleteNight = {
                    viewModel.advancePhase()
                },
                onBackToGrimoire = {
                    viewModel.navigateTo(ScreenDestination.GRIMOIRE)
                },
                onApplySeatAction = { seatNumber, actionType ->
                    viewModel.applySeatNightAction(seatNumber, actionType)
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        ScreenDestination.DAY_COUNCIL -> {
            BackHandler {
                viewModel.navigateTo(ScreenDestination.GRIMOIRE)
            }
            DayPhaseScreen(
                dayNumber = gameState.dayNumber,
                seats = gameState.seats,
                nominations = gameState.nominations,
                activeBlockNomineeSeat = gameState.activeBlockNomineeSeat,
                activeBlockVotes = gameState.activeBlockVotes,
                onBackToGrimoire = {
                    viewModel.navigateTo(ScreenDestination.GRIMOIRE)
                },
                onRecordNomination = { nomination ->
                    viewModel.recordNomination(nomination)
                },
                onExecutePlayer = { seatNumber ->
                    viewModel.executePlayer(seatNumber)
                },
                onPassExecution = {
                    viewModel.passExecution()
                },
                onStartNight = {
                    viewModel.advancePhase()
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        ScreenDestination.ALMANAC -> {
            BackHandler {
                viewModel.navigateTo(ScreenDestination.GRIMOIRE)
            }
            AlmanacScreen(
                currentScript = gameState.scriptName,
                onBackToGrimoire = {
                    viewModel.navigateTo(ScreenDestination.GRIMOIRE)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    if (showNewGameDialog) {
        NewGameDialog(
            onDismiss = { showNewGameDialog = false },
            onCreateGame = { title, script, playerCount ->
                viewModel.createNewGame(title, script, playerCount)
                showNewGameDialog = false
            }
        )
    }

    if (showSavedGamesDialog) {
        SavedGamesDialog(
            games = savedGames,
            currentGameId = gameState.id,
            onSelectGame = { id -> viewModel.loadGame(id) },
            onDeleteGame = { id -> viewModel.deleteGame(id) },
            onDismiss = { showSavedGamesDialog = false }
        )
    }
}

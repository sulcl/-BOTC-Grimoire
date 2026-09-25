package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Alignment as BotcAlignment
import com.example.data.model.BotcCharacter
import com.example.data.model.CharactersData
import com.example.data.model.GamePhase
import com.example.data.model.PlayerSeat
import com.example.ui.components.BotcTokenView
import com.example.ui.components.CharacterPickerDialog
import com.example.ui.components.CircularGrimoireTownSquare
import com.example.ui.components.PlayerInspectorBottomSheet
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.CrimsonBlood
import com.example.ui.theme.CrimsonDeep
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GrimoireBorder
import com.example.ui.theme.GrimoireCard
import com.example.ui.theme.GrimoireFeltDark
import com.example.ui.theme.GrimoireSurface
import com.example.ui.theme.GrimoireSurfaceVariant
import com.example.ui.theme.StateAlive
import com.example.ui.theme.StateDead
import com.example.ui.theme.StateGhostVote
import com.example.ui.theme.TextParchment
import com.example.ui.theme.TextSecondary

enum class GrimoireDisplayMode {
    CIRCLE,
    LIST
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrimoireScreen(
    gameTitle: String,
    scriptName: String,
    phase: GamePhase,
    dayNumber: Int,
    seats: List<PlayerSeat>,
    activeBlockNomineeSeat: Int?,
    activeBlockVotes: Int,
    onUpdateSeat: (PlayerSeat) -> Unit,
    onNavigateToNightRunner: () -> Unit,
    onNavigateToDayCouncil: () -> Unit,
    onNavigateToAlmanac: () -> Unit,
    onNewGameClick: () -> Unit,
    onSavedGamesClick: () -> Unit,
    onAdvancePhase: () -> Unit,
    modifier: Modifier = Modifier
) {
    var displayMode by remember { mutableStateOf(GrimoireDisplayMode.CIRCLE) }
    var inspectingSeat by remember { mutableStateOf<PlayerSeat?>(null) }
    var pickingRoleForSeat by remember { mutableStateOf<PlayerSeat?>(null) }
    var showMenu by remember { mutableStateOf(false) }

    val activeBlockNominee = seats.find { it.seatNumber == activeBlockNomineeSeat }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(
                                text = gameTitle,
                                color = TextParchment,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$scriptName • ${seats.size} Players",
                                color = AntiqueGold,
                                fontSize = 11.sp
                            )
                        }
                    }
                },
                actions = {
                    // View Toggle (Circle vs List)
                    IconButton(
                        onClick = {
                            displayMode = if (displayMode == GrimoireDisplayMode.CIRCLE)
                                GrimoireDisplayMode.LIST
                            else
                                GrimoireDisplayMode.CIRCLE
                        },
                        modifier = Modifier.testTag("toggle_view_mode_button")
                    ) {
                        Icon(
                            imageVector = if (displayMode == GrimoireDisplayMode.CIRCLE)
                                Icons.Default.FormatListBulleted
                            else
                                Icons.Default.Circle,
                            contentDescription = "Toggle Circle or List view",
                            tint = AntiqueGold
                        )
                    }

                    // Almanac shortcut
                    IconButton(
                        onClick = onNavigateToAlmanac,
                        modifier = Modifier.testTag("almanac_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = "Almanac",
                            tint = TextParchment
                        )
                    }

                    // Overflow Menu
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = TextParchment
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier.background(GrimoireSurface)
                        ) {
                            DropdownMenuItem(
                                text = { Text("New Grimoire...", color = TextParchment) },
                                onClick = {
                                    showMenu = false
                                    onNewGameClick()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Add, null, tint = AntiqueGold)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Saved Games...", color = TextParchment) },
                                onClick = {
                                    showMenu = false
                                    onSavedGamesClick()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.Folder, null, tint = AntiqueGold)
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GrimoireSurface,
                    titleContentColor = TextParchment
                )
            )
        },
        bottomBar = {
            // Main Bottom Controls for Quick Real-Time Storyteller Actions
            NavigationBar(
                containerColor = GrimoireSurface,
                contentColor = TextParchment
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToNightRunner,
                    icon = { Icon(Icons.Default.DarkMode, contentDescription = "Night Runner", tint = Color(0xFFCE93D8)) },
                    label = { Text("Night Sheet", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = GrimoireSurfaceVariant
                    ),
                    modifier = Modifier.testTag("nav_night_sheet")
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToDayCouncil,
                    icon = { Icon(Icons.Default.HowToVote, contentDescription = "Day Council", tint = AntiqueGold) },
                    label = { Text("Council & Votes", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = GrimoireSurfaceVariant
                    ),
                    modifier = Modifier.testTag("nav_day_council")
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onAdvancePhase,
                    icon = {
                        Icon(
                            imageVector = if (phase == GamePhase.DAY) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Advance Phase",
                            tint = CrimsonBlood
                        )
                    },
                    label = {
                        Text(
                            text = if (phase == GamePhase.DAY) "To Night" else "To Day",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonBlood
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = CrimsonBlood.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("nav_advance_phase")
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToAlmanac,
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Almanac", tint = GoldGlow) },
                    label = { Text("Almanac", fontSize = 10.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = GrimoireSurfaceVariant
                    )
                )
            }
        },
        containerColor = GrimoireFeltDark,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (displayMode) {
                GrimoireDisplayMode.CIRCLE -> {
                    CircularGrimoireTownSquare(
                        seats = seats,
                        selectedSeat = inspectingSeat,
                        edition = scriptName,
                        phase = phase,
                        dayNumber = dayNumber,
                        activeBlockNominee = activeBlockNominee,
                        onSeatClick = { seat -> inspectingSeat = seat },
                        onCenterActionClick = {
                            if (phase == GamePhase.DAY) {
                                onNavigateToNightRunner()
                            } else {
                                onNavigateToDayCouncil()
                            }
                        }
                    )
                }

                GrimoireDisplayMode.LIST -> {
                    GrimoireListView(
                        seats = seats,
                        onSeatClick = { seat -> inspectingSeat = seat },
                        onToggleAlive = { seat -> onUpdateSeat(seat.copy(isAlive = !seat.isAlive)) },
                        onToggleGhostVote = { seat -> onUpdateSeat(seat.copy(hasGhostVote = !seat.hasGhostVote)) }
                    )
                }
            }
        }
    }

    // Player Inspector Bottom Sheet
    inspectingSeat?.let { seat ->
        // Retrieve fresh state of seat from current list
        val currentSeatState = seats.find { it.seatNumber == seat.seatNumber } ?: seat
        PlayerInspectorBottomSheet(
            seat = currentSeatState,
            onDismiss = { inspectingSeat = null },
            onUpdateSeat = { updated ->
                onUpdateSeat(updated)
                inspectingSeat = updated
            },
            onChangeRoleClick = {
                pickingRoleForSeat = currentSeatState
            }
        )
    }

    // Character Picker Dialog
    pickingRoleForSeat?.let { seat ->
        CharacterPickerDialog(
            currentScript = scriptName,
            onSelectCharacter = { selectedChar ->
                val updated = seat.copy(
                    characterId = selectedChar.id,
                    alignment = selectedChar.defaultAlignment
                )
                onUpdateSeat(updated)
                inspectingSeat = updated
                pickingRoleForSeat = null
            },
            onDismiss = { pickingRoleForSeat = null }
        )
    }
}

@Composable
fun GrimoireListView(
    seats: List<PlayerSeat>,
    onSeatClick: (PlayerSeat) -> Unit,
    onToggleAlive: (PlayerSeat) -> Unit,
    onToggleGhostVote: (PlayerSeat) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(seats, key = { it.seatNumber }) { seat ->
            val character = CharactersData.findCharacter(seat.characterId)
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (seat.isAlive) GrimoireCard else Color(0xFF1D0E13)
                ),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                    1.dp,
                    if (seat.isAlive) (character?.type?.color?.copy(alpha = 0.5f) ?: GrimoireBorder) else CrimsonBlood.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSeatClick(seat) }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    BotcTokenView(
                        seat = seat,
                        character = character,
                        size = 52.dp,
                        showReminders = false,
                        onClick = { onSeatClick(seat) }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Seat #${seat.seatNumber}: ${seat.playerName}",
                                color = TextParchment,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (seat.alignment == BotcAlignment.EVIL) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(CrimsonBlood.copy(alpha = 0.3f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "EVIL",
                                        color = CrimsonBlood,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Text(
                            text = character?.let { "${it.name} (${it.type.label})" } ?: "Unassigned Role",
                            color = character?.type?.color ?: TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        if (seat.reminders.isNotEmpty()) {
                            Text(
                                text = "Tokens: ${seat.reminders.joinToString()}",
                                color = AntiqueGold,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }

                    // Quick Toggle for Alive/Dead
                    Surface(
                        color = if (seat.isAlive) StateAlive.copy(alpha = 0.2f) else CrimsonBlood.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, if (seat.isAlive) StateAlive else CrimsonBlood),
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onToggleAlive(seat) }
                    ) {
                        Text(
                            text = if (seat.isAlive) "ALIVE" else "DEAD",
                            color = if (seat.isAlive) StateAlive else CrimsonBlood,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }

                    if (!seat.isAlive) {
                        Spacer(modifier = Modifier.width(6.dp))
                        // Ghost Vote Toggle
                        Surface(
                            color = if (seat.hasGhostVote) StateGhostVote.copy(alpha = 0.2f) else Color.DarkGray,
                            shape = CircleShape,
                            border = BorderStroke(1.dp, if (seat.hasGhostVote) StateGhostVote else Color.Gray),
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { onToggleGhostVote(seat) }
                                .padding(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HowToVote,
                                contentDescription = "Ghost Vote Toggle",
                                tint = if (seat.hasGhostVote) StateGhostVote else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

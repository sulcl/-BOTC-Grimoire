package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CharactersData
import com.example.data.model.NominationRecord
import com.example.data.model.PlayerSeat
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.CrimsonBlood
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GrimoireBorder
import com.example.ui.theme.GrimoireCard
import com.example.ui.theme.GrimoireFeltDark
import com.example.ui.theme.GrimoireSurface
import com.example.ui.theme.GrimoireSurfaceVariant
import com.example.ui.theme.StateAlive
import com.example.ui.theme.StateGhostVote
import com.example.ui.theme.TextParchment
import com.example.ui.theme.TextSecondary
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayPhaseScreen(
    dayNumber: Int,
    seats: List<PlayerSeat>,
    nominations: List<NominationRecord>,
    activeBlockNomineeSeat: Int?,
    activeBlockVotes: Int,
    onBackToGrimoire: () -> Unit,
    onRecordNomination: (NominationRecord) -> Unit,
    onExecutePlayer: (seatNumber: Int) -> Unit,
    onPassExecution: () -> Unit,
    onStartNight: () -> Unit,
    modifier: Modifier = Modifier
) {
    val aliveCount = seats.count { it.isAlive }
    val majorityThreshold = (aliveCount / 2) + 1

    var showNominationDialog by remember { mutableStateOf(false) }
    var showExecuteConfirmDialog by remember { mutableStateOf(false) }

    val activeBlockNominee = seats.find { it.seatNumber == activeBlockNomineeSeat }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "DAY $dayNumber COUNCIL",
                            color = AntiqueGold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "$aliveCount Alive • Majority: $majorityThreshold votes",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackToGrimoire) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Return to Grimoire",
                            tint = TextParchment
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = onStartNight,
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonBlood),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Call Night", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GrimoireSurface,
                    titleContentColor = TextParchment
                )
            )
        },
        containerColor = GrimoireFeltDark,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Execution / The Block Banner
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (activeBlockNominee != null) Color(0xFF2B0E17) else GrimoireCard
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.5.dp,
                        if (activeBlockNominee != null) CrimsonBlood else GrimoireBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "ON THE BLOCK",
                                color = AntiqueGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            if (activeBlockNominee != null) {
                                Surface(
                                    color = CrimsonBlood,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "$activeBlockVotes Votes (Passed)",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (activeBlockNominee != null) {
                            val char = CharactersData.findCharacter(activeBlockNominee.characterId)
                            Text(
                                text = "Seat #${activeBlockNominee.seatNumber}: ${activeBlockNominee.playerName} (${char?.name ?: "No Role"})",
                                color = TextParchment,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Has the highest vote tally. If no higher vote occurs before dusk, this player will be executed.",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { showExecuteConfirmDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonBlood),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Execute Player", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                OutlinedButton(
                                    onClick = onPassExecution,
                                    border = BorderStroke(1.dp, GrimoireBorder),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextParchment),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Clear Block", fontSize = 12.sp)
                                }
                            }
                        } else {
                            Text(
                                text = "No player currently on the block.",
                                color = TextParchment,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Conduct nominations below. A nominee requires at least $majorityThreshold votes to be eligible for execution.",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
            }

            // Nominations Header and Action
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "NOMINATIONS FOR TODAY",
                        color = AntiqueGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Button(
                        onClick = { showNominationDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GrimoireSurfaceVariant),
                        border = BorderStroke(1.dp, AntiqueGold),
                        modifier = Modifier.testTag("start_nomination_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "New Nomination",
                            tint = AntiqueGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Hold Vote", color = AntiqueGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Nominations History List
            if (nominations.isEmpty()) {
                item {
                    Surface(
                        color = GrimoireCard,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No nominations have taken place yet today.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(nominations) { nom ->
                    val nominator = seats.find { it.seatNumber == nom.nominatorSeat }
                    val nominee = seats.find { it.seatNumber == nom.nomineeSeat }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = GrimoireCard),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(
                            1.dp,
                            if (nom.passedMajority) CrimsonBlood.copy(alpha = 0.6f) else GrimoireBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "${nominator?.playerName ?: "Seat ${nom.nominatorSeat}"} nominated ${nominee?.playerName ?: "Seat ${nom.nomineeSeat}"}",
                                    color = TextParchment,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Surface(
                                    color = if (nom.passedMajority) StateAlive.copy(alpha = 0.2f) else GrimoireBorder.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${nom.votes.size} votes",
                                        color = if (nom.passedMajority) StateAlive else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            if (nom.ghostVotesUsedInThisNomination.isNotEmpty()) {
                                Text(
                                    text = "Ghost votes used by seat: ${nom.ghostVotesUsedInThisNomination.joinToString()}",
                                    color = StateGhostVote,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Hold Vote / Nomination Dialog
    if (showNominationDialog) {
        HoldVoteDialog(
            dayNumber = dayNumber,
            seats = seats,
            majorityThreshold = majorityThreshold,
            onDismiss = { showNominationDialog = false },
            onConfirmNomination = { nomination ->
                onRecordNomination(nomination)
                showNominationDialog = false
            }
        )
    }

    // Execution Confirmation Dialog
    if (showExecuteConfirmDialog && activeBlockNominee != null) {
        val char = CharactersData.findCharacter(activeBlockNominee.characterId)
        AlertDialog(
            onDismissRequest = { showExecuteConfirmDialog = false },
            title = {
                Text(
                    text = "Confirm Execution",
                    color = CrimsonBlood,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Are you sure you want to execute ${activeBlockNominee.playerName} (${char?.name ?: "Unknown"})?",
                        color = TextParchment,
                        fontSize = 14.sp
                    )
                    if (char?.id == "saint") {
                        Text(
                            text = "⚠️ WARNING: This player is the SAINT! If executed, EVIL WINS immediately!",
                            color = Color(0xFFFF5252),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    if (char?.id == "virgin") {
                        Text(
                            text = "Note: Virgin executes nominator if Townsfolk, not themselves.",
                            color = AntiqueGold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onExecutePlayer(activeBlockNominee.seatNumber)
                        showExecuteConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonBlood)
                ) {
                    Text("Execute", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExecuteConfirmDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = GrimoireSurface
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoldVoteDialog(
    dayNumber: Int,
    seats: List<PlayerSeat>,
    majorityThreshold: Int,
    onDismiss: () -> Unit,
    onConfirmNomination: (NominationRecord) -> Unit
) {
    var selectedNominatorSeat by remember { mutableIntStateOf(seats.firstOrNull { it.isAlive }?.seatNumber ?: 1) }
    var selectedNomineeSeat by remember { mutableIntStateOf(seats.getOrNull(1)?.seatNumber ?: 2) }

    val votingSeats = remember { mutableStateListOf<Int>() }
    val ghostVotesUsed = remember { mutableStateListOf<Int>() }

    val currentVotes = votingSeats.size
    val passesMajority = currentVotes >= majorityThreshold

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Hold Nomination Vote",
                color = AntiqueGold,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                // Select Nominator & Nominee
                Text("Nominator (Alive):", color = TextSecondary, fontSize = 11.sp)
                PlayerDropdown(
                    seats = seats.filter { it.isAlive },
                    selectedSeat = selectedNominatorSeat,
                    onSelect = { selectedNominatorSeat = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Nominee:", color = TextSecondary, fontSize = 11.sp)
                PlayerDropdown(
                    seats = seats,
                    selectedSeat = selectedNomineeSeat,
                    onSelect = { selectedNomineeSeat = it }
                )

                HorizontalDivider(color = GrimoireBorder, modifier = Modifier.padding(vertical = 10.dp))

                // Live Voting Threshold Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Votes: $currentVotes / $majorityThreshold required",
                        color = if (passesMajority) StateAlive else TextParchment,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = if (passesMajority) "THRESHOLD MET" else "NOT MET",
                        color = if (passesMajority) StateAlive else CrimsonBlood,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // List of all players to tap for voting
                Text(
                    text = "Tap players who raised their hand:",
                    color = TextSecondary,
                    fontSize = 11.sp
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 4.dp)
                ) {
                    items(seats) { seat ->
                        val isVoted = votingSeats.contains(seat.seatNumber)
                        val canVote = seat.isAlive || seat.hasGhostVote

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .clickable(enabled = canVote) {
                                    if (isVoted) {
                                        votingSeats.remove(seat.seatNumber)
                                        ghostVotesUsed.remove(seat.seatNumber)
                                    } else {
                                        votingSeats.add(seat.seatNumber)
                                        if (!seat.isAlive) {
                                            ghostVotesUsed.add(seat.seatNumber)
                                        }
                                    }
                                }
                                .padding(vertical = 4.dp, horizontal = 6.dp)
                        ) {
                            Checkbox(
                                checked = isVoted,
                                onCheckedChange = null,
                                enabled = canVote,
                                colors = CheckboxDefaults.colors(
                                    checkedColor = AntiqueGold,
                                    uncheckedColor = GrimoireBorder
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Seat #${seat.seatNumber}: ${seat.playerName}",
                                    color = if (canVote) TextParchment else TextSecondary.copy(alpha = 0.5f),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (!seat.isAlive) {
                                    Text(
                                        text = if (seat.hasGhostVote) "(Dead - Ghost vote ready)" else "(Dead - Ghost vote spent)",
                                        color = if (seat.hasGhostVote) StateGhostVote else Color.Gray,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val record = NominationRecord(
                        id = UUID.randomUUID().toString(),
                        dayNumber = dayNumber,
                        nominatorSeat = selectedNominatorSeat,
                        nomineeSeat = selectedNomineeSeat,
                        votes = votingSeats.toList(),
                        ghostVotesUsedInThisNomination = ghostVotesUsed.toList(),
                        passedMajority = passesMajority
                    )
                    onConfirmNomination(record)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonBlood)
            ) {
                Text("Confirm Tally", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        },
        containerColor = GrimoireSurface
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDropdown(
    seats: List<PlayerSeat>,
    selectedSeat: Int,
    onSelect: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val current = seats.find { it.seatNumber == selectedSeat }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = current?.let { "Seat #${it.seatNumber}: ${it.playerName}" } ?: "Select Player",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AntiqueGold,
                unfocusedBorderColor = GrimoireBorder,
                focusedTextColor = TextParchment,
                unfocusedTextColor = TextParchment
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(GrimoireSurface)
        ) {
            seats.forEach { seat ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Seat #${seat.seatNumber}: ${seat.playerName}",
                            color = TextParchment,
                            fontSize = 12.sp
                        )
                    },
                    onClick = {
                        onSelect(seat.seatNumber)
                        expanded = false
                    }
                )
            }
        }
    }
}

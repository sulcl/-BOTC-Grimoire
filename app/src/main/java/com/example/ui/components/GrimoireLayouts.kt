package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Alignment as BotcAlignment
import com.example.data.model.BotcCharacter
import com.example.data.model.CharactersData
import com.example.data.model.GamePhase
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
import com.example.ui.theme.StateDead
import com.example.ui.theme.StateGhostVote
import com.example.ui.theme.TextParchment
import com.example.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularGrimoireTownSquare(
    seats: List<PlayerSeat>,
    selectedSeat: PlayerSeat?,
    edition: String,
    phase: GamePhase,
    dayNumber: Int,
    activeBlockNominee: PlayerSeat?,
    onSeatClick: (PlayerSeat) -> Unit,
    onCenterActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF241017),
                        GrimoireFeltDark
                    )
                )
            )
            .padding(16.dp)
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        // Responsive radius calculation
        val minDim = minOf(widthPx, heightPx)
        val tokenSize = when {
            minDim < 900 -> 68.dp
            minDim < 1300 -> 80.dp
            else -> 92.dp
        }

        // Center Town Square Plaque
        TownSquareCenterPlaque(
            edition = edition,
            phase = phase,
            dayNumber = dayNumber,
            aliveCount = seats.count { it.isAlive },
            deadCount = seats.count { !it.isAlive },
            activeBlockNominee = activeBlockNominee,
            onActionClick = onCenterActionClick,
            modifier = Modifier.size(minOf(maxWidth * 0.42f, 190.dp))
        )

        // Radial seating
        val count = seats.size
        val radiusX = (maxWidth.value / 2f) - (tokenSize.value / 1.7f)
        val radiusY = (maxHeight.value / 2f) - (tokenSize.value / 1.6f)

        seats.forEachIndexed { index, seat ->
            // Clockwise from top (starts at -90 degrees)
            val angle = (2 * Math.PI / count) * index - (Math.PI / 2)
            val offsetX = (radiusX * cos(angle)).dp
            val offsetY = (radiusY * sin(angle)).dp

            val character = CharactersData.findCharacter(seat.characterId)

            Box(
                modifier = Modifier
                    .offset(x = offsetX, y = offsetY),
                contentAlignment = Alignment.Center
            ) {
                BotcTokenView(
                    seat = seat,
                    character = character,
                    size = tokenSize,
                    isSelected = selectedSeat?.seatNumber == seat.seatNumber,
                    onClick = { onSeatClick(seat) }
                )
            }
        }
    }
}

@Composable
fun TownSquareCenterPlaque(
    edition: String,
    phase: GamePhase,
    dayNumber: Int,
    aliveCount: Int,
    deadCount: Int,
    activeBlockNominee: PlayerSeat?,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xF0180B0F),
        shape = CircleShape,
        border = BorderStroke(2.dp, Brush.radialGradient(listOf(AntiqueGold, CrimsonBlood))),
        shadowElevation = 8.dp,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = "TOWN SQUARE",
                color = AntiqueGold,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Text(
                text = when (phase) {
                    GamePhase.SETUP -> "SETUP"
                    GamePhase.FIRST_NIGHT -> "FIRST NIGHT"
                    GamePhase.NIGHT -> "NIGHT $dayNumber"
                    GamePhase.DAY -> "DAY $dayNumber"
                },
                color = TextParchment,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            // Alive vs Dead
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "$aliveCount Alive",
                    color = StateAlive,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " • ",
                    color = TextSecondary,
                    fontSize = 10.sp
                )
                Text(
                    text = "$deadCount Dead",
                    color = Color(0xFFFF8A80),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (activeBlockNominee != null) {
                Text(
                    text = "ON THE BLOCK:\n${activeBlockNominee.playerName}",
                    color = CrimsonBlood,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 11.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            // Quick Runner Action Button
            Surface(
                color = CrimsonBlood,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onActionClick)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = if (phase == GamePhase.DAY) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Switch or Run Phase",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (phase == GamePhase.DAY) "Run Night" else "Run Day",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlayerInspectorBottomSheet(
    seat: PlayerSeat,
    onDismiss: () -> Unit,
    onUpdateSeat: (PlayerSeat) -> Unit,
    onChangeRoleClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val character = CharactersData.findCharacter(seat.characterId)

    var playerName by remember(seat) { mutableStateOf(seat.playerName) }
    var notes by remember(seat) { mutableStateOf(seat.notes) }
    var customReminderText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = GrimoireSurface,
        scrimColor = Color(0x99000000)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header: Seat + Name + Role
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                BotcTokenView(
                    seat = seat,
                    character = character,
                    size = 72.dp,
                    showReminders = false,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Seat #${seat.seatNumber}",
                        color = AntiqueGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = playerName,
                        onValueChange = {
                            playerName = it
                            onUpdateSeat(seat.copy(playerName = it))
                        },
                        label = { Text("Player Name", color = TextSecondary) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AntiqueGold,
                            unfocusedBorderColor = GrimoireBorder,
                            focusedTextColor = TextParchment,
                            unfocusedTextColor = TextParchment
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Role & Alignment Action Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onChangeRoleClick,
                    colors = ButtonDefaults.buttonColors(containerColor = GrimoireSurfaceVariant),
                    border = BorderStroke(1.dp, AntiqueGold),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Change Role",
                        tint = AntiqueGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = character?.name ?: "Assign Role",
                        color = TextParchment,
                        fontSize = 12.sp
                    )
                }

                // Alignment toggle button
                OutlinedButton(
                    onClick = {
                        val next = if (seat.alignment == BotcAlignment.GOOD) BotcAlignment.EVIL else BotcAlignment.GOOD
                        onUpdateSeat(seat.copy(alignment = next))
                    },
                    border = BorderStroke(
                        1.dp,
                        if (seat.alignment == BotcAlignment.EVIL) CrimsonBlood else Color(0xFF42A5F5)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Team: ${seat.alignment.label}",
                        color = if (seat.alignment == BotcAlignment.EVIL) CrimsonBlood else Color(0xFF42A5F5),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            if (character != null) {
                Surface(
                    color = GrimoireCard,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, GrimoireBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "${character.type.label} • ${character.edition}",
                            color = character.type.color,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = character.ability,
                            color = TextParchment,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            HorizontalDivider(color = GrimoireBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Alive & Ghost Vote Toggles
            Text(
                text = "STATUS & GHOST VOTE",
                color = AntiqueGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (seat.isAlive) Icons.Default.Favorite else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (seat.isAlive) StateAlive else CrimsonBlood,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (seat.isAlive) "Player is ALIVE" else "Player is DEAD (Shrouded)",
                        color = TextParchment,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Switch(
                    checked = seat.isAlive,
                    onCheckedChange = { isAlive ->
                        onUpdateSeat(seat.copy(isAlive = isAlive))
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = StateAlive,
                        checkedTrackColor = StateAlive.copy(alpha = 0.4f),
                        uncheckedThumbColor = CrimsonBlood,
                        uncheckedTrackColor = Color.DarkGray
                    )
                )
            }

            // Ghost vote toggle (relevant if dead)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.HowToVote,
                        contentDescription = null,
                        tint = if (seat.hasGhostVote) StateGhostVote else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (seat.hasGhostVote) "Ghost Vote Available" else "Ghost Vote Spent",
                        color = if (seat.hasGhostVote) TextParchment else TextSecondary,
                        fontSize = 13.sp
                    )
                }
                Switch(
                    checked = seat.hasGhostVote,
                    onCheckedChange = { hasVote ->
                        onUpdateSeat(seat.copy(hasGhostVote = hasVote))
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = StateGhostVote,
                        checkedTrackColor = StateGhostVote.copy(alpha = 0.4f)
                    )
                )
            }

            HorizontalDivider(color = GrimoireBorder, modifier = Modifier.padding(vertical = 8.dp))

            // Reminder Tokens Section
            Text(
                text = "ACTIVE REMINDER TOKENS",
                color = AntiqueGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                if (seat.reminders.isEmpty()) {
                    Text(
                        text = "No reminder tokens attached to this seat.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                } else {
                    seat.reminders.forEach { reminder ->
                        ReminderChip(
                            text = reminder,
                            onRemove = {
                                val updated = seat.reminders.filter { it != reminder }
                                onUpdateSeat(seat.copy(reminders = updated))
                            }
                        )
                    }
                }
            }

            // Quick Add Suggested Reminders
            val suggested = buildList {
                character?.defaultReminders?.let { addAll(it) }
                addAll(listOf("Poisoned", "Drunk", "Protected", "Nominated", "Dead"))
            }.distinct()

            Text(
                text = "Quick Add Reminder:",
                color = TextSecondary,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                suggested.forEach { tokenText ->
                    val isAlreadyAdded = seat.reminders.contains(tokenText)
                    FilterChip(
                        selected = isAlreadyAdded,
                        onClick = {
                            val updated = if (isAlreadyAdded) {
                                seat.reminders.filter { it != tokenText }
                            } else {
                                seat.reminders + tokenText
                            }
                            onUpdateSeat(seat.copy(reminders = updated))
                        },
                        label = { Text(tokenText, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AntiqueGold.copy(alpha = 0.3f),
                            selectedLabelColor = GoldGlow,
                            containerColor = GrimoireSurfaceVariant,
                            labelColor = TextParchment
                        ),
                        border = BorderStroke(1.dp, if (isAlreadyAdded) AntiqueGold else GrimoireBorder)
                    )
                }
            }

            // Custom Reminder text field
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                OutlinedTextField(
                    value = customReminderText,
                    onValueChange = { customReminderText = it },
                    placeholder = { Text("Custom reminder...", color = TextSecondary, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AntiqueGold,
                        unfocusedBorderColor = GrimoireBorder,
                        focusedTextColor = TextParchment,
                        unfocusedTextColor = TextParchment
                    ),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (customReminderText.isNotBlank()) {
                            onUpdateSeat(seat.copy(reminders = seat.reminders + customReminderText.trim()))
                            customReminderText = ""
                        }
                    },
                    modifier = Modifier
                        .background(AntiqueGold, CircleShape)
                        .size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add custom reminder",
                        tint = Color.Black
                    )
                }
            }

            HorizontalDivider(color = GrimoireBorder, modifier = Modifier.padding(vertical = 12.dp))

            // Storyteller Notes for this player
            Text(
                text = "STORYTELLER PRIVATE NOTES",
                color = AntiqueGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = notes,
                onValueChange = {
                    notes = it
                    onUpdateSeat(seat.copy(notes = it))
                },
                placeholder = {
                    Text(
                        "e.g. Shown Alice & Bob as Empath '1'; Wash pinged Bob as Investigator...",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AntiqueGold,
                    unfocusedBorderColor = GrimoireBorder,
                    focusedTextColor = TextParchment,
                    unfocusedTextColor = TextParchment
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                minLines = 2
            )
        }
    }
}

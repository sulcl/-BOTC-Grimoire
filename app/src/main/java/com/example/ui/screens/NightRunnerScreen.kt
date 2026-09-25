package com.example.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.data.model.BotcCharacter
import com.example.data.model.CharactersData
import com.example.data.model.GamePhase
import com.example.data.model.NightStep
import com.example.data.model.PlayerSeat
import com.example.ui.components.ReminderChip
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.CrimsonBlood
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GrimoireBorder
import com.example.ui.theme.GrimoireCard
import com.example.ui.theme.GrimoireFeltDark
import com.example.ui.theme.GrimoireSurface
import com.example.ui.theme.GrimoireSurfaceVariant
import com.example.ui.theme.TextParchment
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NightRunnerScreen(
    phase: GamePhase,
    dayNumber: Int,
    seats: List<PlayerSeat>,
    onCompleteNight: () -> Unit,
    onBackToGrimoire: () -> Unit,
    onApplySeatAction: (seatNumber: Int, actionType: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isFirstNight = (phase == GamePhase.FIRST_NIGHT)

    // Build the ordered night steps based on characters in play
    val nightSteps = remember(isFirstNight, seats) {
        buildNightSteps(isFirstNight, seats)
    }

    var currentStepIndex by remember { mutableIntStateOf(0) }
    val currentStep = nightSteps.getOrNull(currentStepIndex)
    val progress = if (nightSteps.isNotEmpty()) (currentStepIndex + 1).toFloat() / nightSteps.size else 1f

    val character = CharactersData.findCharacter(currentStep?.characterId)
    val assignedSeat = seats.find { it.characterId == currentStep?.characterId }

    fun triggerVibration() {
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(40)
            }
        } catch (_: Exception) {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isFirstNight) "FIRST NIGHT RUNNER" else "NIGHT $dayNumber RUNNER",
                            color = AntiqueGold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Step ${currentStepIndex + 1} of ${nightSteps.size}",
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
                    OutlinedButton(
                        onClick = {
                            triggerVibration()
                            onCompleteNight()
                        },
                        border = BorderStroke(1.dp, CrimsonBlood),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LightMode,
                            contentDescription = null,
                            tint = CrimsonBlood,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Dawn / Day", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = AntiqueGold,
                trackColor = GrimoireSurfaceVariant
            )

            if (currentStep == null || nightSteps.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No waking characters scheduled for this night.",
                        color = TextParchment,
                        fontSize = 14.sp
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Main Card for Current Wake Step
                    Card(
                        colors = CardDefaults.cardColors(containerColor = GrimoireCard),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, character?.type?.color ?: AntiqueGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            item {
                                // Character Header
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Official Icon via Coil with Monogram Fallback
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF190A0F))
                                            .border(2.dp, character?.type?.color ?: AntiqueGold, CircleShape)
                                    ) {
                                        if (character != null) {
                                            SubcomposeAsyncImage(
                                                model = character.officialIconUrl,
                                                contentDescription = character.name,
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier.size(54.dp),
                                                loading = {
                                                    Text(
                                                        text = character.name.take(2).uppercase(),
                                                        color = character.type.color,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )
                                                },
                                                error = {
                                                    Text(
                                                        text = character.name.take(2).uppercase(),
                                                        color = character.type.color,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )
                                                }
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.DarkMode,
                                                contentDescription = null,
                                                tint = AntiqueGold,
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = currentStep.title,
                                            color = character?.type?.color ?: GoldGlow,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (assignedSeat != null) {
                                            Text(
                                                text = "Seat #${assignedSeat.seatNumber}: ${assignedSeat.playerName}",
                                                color = TextParchment,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            // Drunk / Poisoned warning banner
                                            val isPoisoned = assignedSeat.reminders.any { it.contains("Poison", ignoreCase = true) }
                                            val isDrunk = assignedSeat.reminders.any { it.contains("Drunk", ignoreCase = true) }
                                            if (isPoisoned || isDrunk) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier
                                                        .padding(top = 2.dp)
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(CrimsonBlood.copy(alpha = 0.3f))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Warning,
                                                        contentDescription = null,
                                                        tint = CrimsonBlood,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = if (isPoisoned) "POISONED (Fake info / no effect)" else "DRUNK (Fake info)",
                                                        color = Color(0xFFFF8A80),
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }

                                HorizontalDivider(
                                    color = GrimoireBorder,
                                    modifier = Modifier.padding(vertical = 12.dp)
                                )

                                // Instructions Text
                                Text(
                                    text = "STORYTELLER SCRIPT:",
                                    color = AntiqueGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )

                                Text(
                                    text = currentStep.reminderText,
                                    color = TextParchment,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                                if (character != null) {
                                    Surface(
                                        color = GrimoireSurfaceVariant,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(
                                                text = "Character Ability:",
                                                color = character.type.color,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = character.ability,
                                                color = TextSecondary,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                    }
                                }

                                // Interactive Quick Storyteller Actions for this step
                                Text(
                                    text = "QUICK ACTIONS:",
                                    color = AntiqueGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
                                )

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    if (currentStep.characterId == "poisoner") {
                                        QuickActionButton(
                                            label = "Mark Poisoned",
                                            onClick = { onApplySeatAction(assignedSeat?.seatNumber ?: 1, "POISON") }
                                        )
                                    }
                                    if (currentStep.characterId == "monk") {
                                        QuickActionButton(
                                            label = "Mark Protected",
                                            onClick = { onApplySeatAction(assignedSeat?.seatNumber ?: 1, "PROTECT") }
                                        )
                                    }
                                    if (currentStep.characterId == "imp") {
                                        QuickActionButton(
                                            label = "Mark Demon Kill",
                                            onClick = { onApplySeatAction(assignedSeat?.seatNumber ?: 1, "KILL") }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step Navigation Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (currentStepIndex > 0) {
                                    triggerVibration()
                                    currentStepIndex--
                                }
                            },
                            enabled = currentStepIndex > 0,
                            border = BorderStroke(1.dp, if (currentStepIndex > 0) AntiqueGold else Color.DarkGray),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextParchment),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Previous Step",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Back", fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                triggerVibration()
                                if (currentStepIndex < nightSteps.size - 1) {
                                    currentStepIndex++
                                } else {
                                    onCompleteNight()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonBlood),
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("night_step_next_button")
                        ) {
                            Text(
                                text = if (currentStepIndex < nightSteps.size - 1) "Next Step" else "Complete Night",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = if (currentStepIndex < nightSteps.size - 1)
                                    Icons.AutoMirrored.Filled.ArrowForward
                                else
                                    Icons.Default.Check,
                                contentDescription = "Next or Complete",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionButton(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        color = GrimoireSurfaceVariant,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, AntiqueGold),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            color = AntiqueGold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

private fun buildNightSteps(isFirstNight: Boolean, seats: List<PlayerSeat>): List<NightStep> {
    val steps = mutableListOf<NightStep>()

    if (isFirstNight) {
        // Minion and Demon Info Steps
        steps.add(
            NightStep(
                stepId = "minion_info",
                characterId = null,
                title = "Minion Info",
                reminderText = "Wake all Minions together. Show 'THIS IS THE DEMON' token, then point to the Demon. Put Minions to sleep.",
                wakesIfRoleInPlayOnly = false
            )
        )
        steps.add(
            NightStep(
                stepId = "demon_info",
                characterId = null,
                title = "Demon Info",
                reminderText = "Wake the Demon. Show 'THESE ARE YOUR MINIONS' token, point to Minions. Show 3 not-in-play Good character bluffs. Put Demon to sleep.",
                wakesIfRoleInPlayOnly = false
            )
        )

        // Find characters in play that wake on First Night, sorted by firstNightOrder
        val wakingChars = seats.mapNotNull { seat ->
            val char = CharactersData.findCharacter(seat.characterId)
            if (char?.firstNightOrder != null) char else null
        }.distinctBy { it.id }
            .sortedBy { it.firstNightOrder ?: 999 }

        for (char in wakingChars) {
            steps.add(
                NightStep(
                    stepId = "wake_${char.id}",
                    characterId = char.id,
                    title = char.name,
                    reminderText = char.firstNightReminder ?: "Wake ${char.name} and execute their ability.",
                    wakesIfRoleInPlayOnly = true
                )
            )
        }
    } else {
        // Other nights
        val wakingChars = seats.mapNotNull { seat ->
            val char = CharactersData.findCharacter(seat.characterId)
            // Even if dead, some characters like Ravenkeeper wake if killed tonight!
            if (char?.otherNightsOrder != null) char else null
        }.distinctBy { it.id }
            .sortedBy { it.otherNightsOrder ?: 999 }

        for (char in wakingChars) {
            steps.add(
                NightStep(
                    stepId = "wake_${char.id}",
                    characterId = char.id,
                    title = char.name,
                    reminderText = char.otherNightsReminder ?: "Wake ${char.name} and execute their ability.",
                    wakesIfRoleInPlayOnly = true
                )
            )
        }
    }

    return steps
}

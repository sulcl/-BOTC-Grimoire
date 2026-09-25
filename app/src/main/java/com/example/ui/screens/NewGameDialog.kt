package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CharactersData
import com.example.data.model.ScriptDistribution
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.CrimsonBlood
import com.example.ui.theme.GrimoireBorder
import com.example.ui.theme.GrimoireSurface
import com.example.ui.theme.GrimoireSurfaceVariant
import com.example.ui.theme.RoleDemon
import com.example.ui.theme.RoleMinion
import com.example.ui.theme.RoleOutsider
import com.example.ui.theme.RoleTownsfolk
import com.example.ui.theme.TextParchment
import com.example.ui.theme.TextSecondary
import kotlin.math.roundToInt

@Composable
fun NewGameDialog(
    onDismiss: () -> Unit,
    onCreateGame: (title: String, script: String, playerCount: Int) -> Unit
) {
    var gameTitle by remember { mutableStateOf("Game #${(100..999).random()}") }
    var selectedScript by remember { mutableStateOf(CharactersData.SCRIPT_TROUBLE_BREWING) }
    var playerCountSlider by remember { mutableFloatStateOf(8f) }

    val playerCount = playerCountSlider.roundToInt()
    val distribution = ScriptDistribution.forPlayerCount(playerCount)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "NEW GRIMOIRE SETUP",
                color = AntiqueGold,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Game Title
                OutlinedTextField(
                    value = gameTitle,
                    onValueChange = { gameTitle = it },
                    label = { Text("Game Title", color = TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AntiqueGold,
                        unfocusedBorderColor = GrimoireBorder,
                        focusedTextColor = TextParchment,
                        unfocusedTextColor = TextParchment
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("game_title_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Script Selection
                Text(
                    text = "SELECT SCRIPT / EDITION:",
                    color = AntiqueGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    CharactersData.ALL_SCRIPTS.forEach { script ->
                        val isSelected = selectedScript == script
                        Surface(
                            color = if (isSelected) AntiqueGold.copy(alpha = 0.25f) else GrimoireSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (isSelected) AntiqueGold else GrimoireBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedScript = script }
                        ) {
                            Text(
                                text = script,
                                color = if (isSelected) AntiqueGold else TextParchment,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                HorizontalDivider(color = GrimoireBorder, modifier = Modifier.padding(vertical = 12.dp))

                // Player Count Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PLAYER COUNT:",
                        color = AntiqueGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$playerCount Players",
                        color = TextParchment,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Slider(
                    value = playerCountSlider,
                    onValueChange = { playerCountSlider = it },
                    valueRange = 5f..15f,
                    steps = 9,
                    colors = SliderDefaults.colors(
                        thumbColor = AntiqueGold,
                        activeTrackColor = CrimsonBlood,
                        inactiveTrackColor = GrimoireSurfaceVariant
                    ),
                    modifier = Modifier.testTag("player_count_slider")
                )

                // BOTC Official Base Distribution Breakdown
                Surface(
                    color = GrimoireSurfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CompositionBadge(count = distribution.townsfolk, label = "Townsfolk", color = RoleTownsfolk)
                        CompositionBadge(count = distribution.outsiders, label = "Outsider", color = RoleOutsider)
                        CompositionBadge(count = distribution.minions, label = "Minion", color = RoleMinion)
                        CompositionBadge(count = distribution.demons, label = "Demon", color = RoleDemon)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalTitle = if (gameTitle.isBlank()) "Grimoire $selectedScript" else gameTitle.trim()
                    onCreateGame(finalTitle, selectedScript, playerCount)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CrimsonBlood),
                modifier = Modifier.testTag("create_game_confirm_button")
            ) {
                Text("Start Grimoire", fontWeight = FontWeight.Bold)
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

@Composable
fun CompositionBadge(count: Int, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$count",
            color = color,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp
        )
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 9.sp
        )
    }
}

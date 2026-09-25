package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.data.model.Alignment as BotcAlignment
import com.example.data.model.BotcCharacter
import com.example.data.model.CharactersData
import com.example.data.model.CharacterType
import com.example.data.model.PlayerSeat
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.CrimsonBlood
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GrimoireBorder
import com.example.ui.theme.GrimoireCard
import com.example.ui.theme.RoleDemon
import com.example.ui.theme.RoleMinion
import com.example.ui.theme.RoleOutsider
import com.example.ui.theme.RoleTownsfolk
import com.example.ui.theme.RoleTraveler
import com.example.ui.theme.StateGhostVote
import com.example.ui.theme.StateGhostVoteSpent
import com.example.ui.theme.TextParchment
import com.example.ui.theme.TextSecondary

@Composable
fun BotcTokenView(
    seat: PlayerSeat,
    character: BotcCharacter?,
    modifier: Modifier = Modifier,
    size: Dp = 88.dp,
    showReminders: Boolean = true,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val isDead = !seat.isAlive
    val hasGhostVote = seat.hasGhostVote
    val tokenAlpha by animateFloatAsState(targetValue = if (isDead) 0.65f else 1.0f, label = "alpha")

    val typeColor = when (character?.type) {
        CharacterType.TOWNSFOLK -> RoleTownsfolk
        CharacterType.OUTSIDER -> RoleOutsider
        CharacterType.MINION -> RoleMinion
        CharacterType.DEMON -> RoleDemon
        CharacterType.TRAVELER -> RoleTraveler
        else -> AntiqueGold
    }

    val borderBrush = if (isSelected) {
        Brush.sweepGradient(listOf(GoldGlow, CrimsonBlood, GoldGlow))
    } else if (seat.alignment == BotcAlignment.EVIL) {
        Brush.linearGradient(listOf(CrimsonBlood, Color(0xFF6A0C16)))
    } else {
        Brush.linearGradient(listOf(AntiqueGold, Color(0xFF6B581B)))
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .testTag("seat_token_${seat.seatNumber}")
            .clickable(onClick = onClick)
    ) {
        // Seat Number & Player Name Label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(bottom = 2.dp)
                .background(Color(0xCC1A0B10), RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(AntiqueGold)
            ) {
                Text(
                    text = "${seat.seatNumber}",
                    color = Color.Black,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = seat.playerName,
                color = TextParchment,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Circular Token
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .alpha(tokenAlpha)
                .shadow(elevation = if (isSelected) 8.dp else 4.dp, shape = CircleShape)
                .border(
                    width = if (isSelected) 3.dp else 2.dp,
                    brush = borderBrush,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF2C161D),
                            Color(0xFF14080B)
                        )
                    )
                )
        ) {
            if (character != null) {
                // Official Character Icon via Coil with Fallback
                SubcomposeAsyncImage(
                    model = character.officialIconUrl,
                    contentDescription = character.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize(0.75f)
                        .clip(CircleShape),
                    loading = {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = character.name.take(2).uppercase(),
                                color = typeColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    },
                    error = {
                        // High-contrast gothic fallback monogram
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = character.name.take(2).uppercase(),
                                color = typeColor,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = character.type.label.take(1),
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        }
                    }
                )
            } else {
                // Empty / Unassigned seat
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Unassigned seat",
                        tint = TextSecondary.copy(alpha = 0.5f),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Unassigned",
                        color = TextSecondary.copy(alpha = 0.5f),
                        fontSize = 9.sp
                    )
                }
            }

            // Top Role Type Strip
            if (character != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .width(size * 0.55f)
                        .height(5.dp)
                        .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                        .background(typeColor)
                )
            }

            // Shroud for Dead Player
            if (isDead) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x99000000)),
                    contentAlignment = Alignment.Center
                ) {
                    // Shroud Ribbon overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(18.dp)
                            .background(Color(0xD91E090F), RoundedCornerShape(4.dp))
                            .border(1.dp, CrimsonBlood.copy(alpha = 0.6f), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SHROUDED",
                            color = Color(0xFFFF8A80),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // Dead Ghost Vote Token Indicator on top of Shroud
            if (isDead) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(if (hasGhostVote) StateGhostVote else StateGhostVoteSpent)
                        .border(1.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HowToVote,
                        contentDescription = if (hasGhostVote) "Ghost vote ready" else "Ghost vote spent",
                        tint = if (hasGhostVote) Color.Black else Color(0xFF9E9E9E),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // Evil Alignment Indicator Badge (for Storyteller eye only)
            if (seat.alignment == BotcAlignment.EVIL) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(CrimsonBlood)
                )
            }
        }

        // Character Name text
        Text(
            text = character?.name ?: "No Role",
            color = if (character != null) typeColor else TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 2.dp)
        )

        // Perceived role (if Drunk or Lunatic)
        if (seat.perceivedCharacterId != null && seat.perceivedCharacterId != seat.characterId) {
            val perceived = CharactersData.findCharacter(seat.perceivedCharacterId)
            if (perceived != null) {
                Text(
                    text = "thinks: ${perceived.name}",
                    color = AntiqueGold,
                    fontSize = 9.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Reminder tokens badges
        if (showReminders && seat.reminders.isNotEmpty()) {
            ReminderTokensRow(reminders = seat.reminders, maxDisplay = 2)
        }
    }
}

@Composable
fun ReminderTokensRow(
    reminders: List<String>,
    modifier: Modifier = Modifier,
    maxDisplay: Int = 3
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(top = 2.dp)
    ) {
        reminders.take(maxDisplay).forEach { reminder ->
            val color = when {
                reminder.contains("Poison", ignoreCase = true) -> Color(0xFFAB47BC)
                reminder.contains("Drunk", ignoreCase = true) -> Color(0xFFFFB74D)
                reminder.contains("Protect", ignoreCase = true) -> Color(0xFF4FC3F7)
                reminder.contains("Dead", ignoreCase = true) -> Color(0xFFEF5350)
                reminder.contains("Mad", ignoreCase = true) -> Color(0xFFFF7043)
                else -> AntiqueGold
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 1.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color.copy(alpha = 0.25f))
                    .border(1.dp, color, RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = reminder,
                    color = color,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
        if (reminders.size > maxDisplay) {
            Text(
                text = "+${reminders.size - maxDisplay}",
                color = TextSecondary,
                fontSize = 8.sp,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
    }
}

@Composable
fun ReminderChip(
    text: String,
    onRemove: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val color = when {
        text.contains("Poison", ignoreCase = true) -> Color(0xFFBA68C8)
        text.contains("Drunk", ignoreCase = true) -> Color(0xFFFFB74D)
        text.contains("Protect", ignoreCase = true) -> Color(0xFF4FC3F7)
        text.contains("Dead", ignoreCase = true) -> Color(0xFFE57373)
        text.contains("Mad", ignoreCase = true) -> Color(0xFFFF8A65)
        text.contains("Vote", ignoreCase = true) -> Color(0xFF81C784)
        else -> AntiqueGold
    }

    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, color),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = text,
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (onRemove != null) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove reminder $text",
                    tint = color,
                    modifier = Modifier
                        .size(14.dp)
                        .clickable(onClick = onRemove)
                )
            }
        }
    }
}

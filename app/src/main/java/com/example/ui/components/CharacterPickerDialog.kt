package com.example.ui.components

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import com.example.data.model.BotcCharacter
import com.example.data.model.CharacterType
import com.example.data.model.CharactersData
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.CrimsonBlood
import com.example.ui.theme.GrimoireBorder
import com.example.ui.theme.GrimoireCard
import com.example.ui.theme.GrimoireSurface
import com.example.ui.theme.GrimoireSurfaceVariant
import com.example.ui.theme.TextParchment
import com.example.ui.theme.TextSecondary

@Composable
fun CharacterPickerDialog(
    currentScript: String,
    onSelectCharacter: (BotcCharacter) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }

    val categories = listOf("All", "Townsfolk", "Outsider", "Minion", "Demon", "Traveler")

    val scriptCharacters = remember(currentScript) {
        CharactersData.getCharactersForScript(currentScript)
    }

    val filtered = remember(searchQuery, selectedCategoryIndex, scriptCharacters) {
        scriptCharacters.filter { char ->
            val matchesSearch = searchQuery.isBlank() ||
                    char.name.contains(searchQuery, ignoreCase = true) ||
                    char.ability.contains(searchQuery, ignoreCase = true)

            val matchesCategory = when (selectedCategoryIndex) {
                0 -> true
                1 -> char.type == CharacterType.TOWNSFOLK
                2 -> char.type == CharacterType.OUTSIDER
                3 -> char.type == CharacterType.MINION
                4 -> char.type == CharacterType.DEMON
                5 -> char.type == CharacterType.TRAVELER
                else -> true
            }

            matchesSearch && matchesCategory
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.88f)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, AntiqueGold, RoundedCornerShape(16.dp)),
            color = GrimoireSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "ASSIGN CHARACTER",
                            color = AntiqueGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Script: $currentScript",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close character picker",
                            tint = TextParchment
                        )
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search character or ability...", color = TextSecondary, fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = AntiqueGold
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = TextSecondary
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AntiqueGold,
                        unfocusedBorderColor = GrimoireBorder,
                        focusedTextColor = TextParchment,
                        unfocusedTextColor = TextParchment,
                        focusedContainerColor = GrimoireSurfaceVariant,
                        unfocusedContainerColor = GrimoireSurfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true
                )

                // Category Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedCategoryIndex,
                    containerColor = GrimoireSurface,
                    contentColor = AntiqueGold,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedCategoryIndex]),
                            color = AntiqueGold
                        )
                    },
                    edgePadding = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedCategoryIndex == index,
                            onClick = { selectedCategoryIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    color = if (selectedCategoryIndex == index) AntiqueGold else TextSecondary,
                                    fontWeight = if (selectedCategoryIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Grid of Characters
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 130.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filtered, key = { it.id }) { character ->
                        CharacterPickCard(
                            character = character,
                            onClick = { onSelectCharacter(character) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterPickCard(
    character: BotcCharacter,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = GrimoireCard),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, character.type.color.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            // Official Icon via Coil with Monogram Fallback
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF16090D))
                    .border(1.5.dp, character.type.color, CircleShape)
            ) {
                SubcomposeAsyncImage(
                    model = character.officialIconUrl,
                    contentDescription = character.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(46.dp),
                    loading = {
                        Text(
                            text = character.name.take(2).uppercase(),
                            color = character.type.color,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    },
                    error = {
                        Text(
                            text = character.name.take(2).uppercase(),
                            color = character.type.color,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = character.name,
                color = TextParchment,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = character.type.label,
                color = character.type.color,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = character.ability,
                color = TextSecondary,
                fontSize = 10.sp,
                lineHeight = 13.sp,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

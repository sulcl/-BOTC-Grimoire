package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.data.model.BotcCharacter
import com.example.data.model.CharacterType
import com.example.data.model.CharactersData
import com.example.ui.components.ReminderChip
import com.example.ui.theme.AntiqueGold
import com.example.ui.theme.GrimoireBorder
import com.example.ui.theme.GrimoireCard
import com.example.ui.theme.GrimoireFeltDark
import com.example.ui.theme.GrimoireSurface
import com.example.ui.theme.GrimoireSurfaceVariant
import com.example.ui.theme.TextParchment
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlmanacScreen(
    currentScript: String,
    onBackToGrimoire: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var selectedScript by remember { mutableStateOf(currentScript) }

    val categories = listOf("All", "Townsfolk", "Outsider", "Minion", "Demon")

    val scriptCharacters = remember(selectedScript) {
        CharactersData.getCharactersForScript(selectedScript)
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
                else -> true
            }

            matchesSearch && matchesCategory
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CHARACTER ALMANAC",
                        color = AntiqueGold,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
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
            // Script Selector Tabs
            ScrollableTabRow(
                selectedTabIndex = CharactersData.ALL_SCRIPTS.indexOf(selectedScript).coerceAtLeast(0),
                containerColor = GrimoireSurface,
                contentColor = AntiqueGold,
                indicator = { tabPositions ->
                    val idx = CharactersData.ALL_SCRIPTS.indexOf(selectedScript).coerceAtLeast(0)
                    if (idx < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[idx]),
                            color = AntiqueGold
                        )
                    }
                },
                edgePadding = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                CharactersData.ALL_SCRIPTS.forEach { script ->
                    val isSelected = selectedScript == script
                    Tab(
                        selected = isSelected,
                        onClick = { selectedScript = script },
                        text = {
                            Text(
                                text = script,
                                color = if (isSelected) AntiqueGold else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search character abilities...", color = TextSecondary, fontSize = 13.sp) },
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
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )

            // Category Filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEachIndexed { index, name ->
                    val isSelected = selectedCategoryIndex == index
                    Surface(
                        color = if (isSelected) AntiqueGold else GrimoireSurfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedCategoryIndex = index }
                    ) {
                        Text(
                            text = name,
                            color = if (isSelected) Color.Black else TextParchment,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Character Cards List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filtered, key = { it.id }) { character ->
                    AlmanacCharacterCard(character = character)
                }
            }
        }
    }
}

@Composable
fun AlmanacCharacterCard(character: BotcCharacter) {
    Card(
        colors = CardDefaults.cardColors(containerColor = GrimoireCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, character.type.color.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF14080B))
                        .border(1.5.dp, character.type.color, CircleShape)
                ) {
                    SubcomposeAsyncImage(
                        model = character.officialIconUrl,
                        contentDescription = character.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(44.dp),
                        loading = {
                            Text(
                                text = character.name.take(2).uppercase(),
                                color = character.type.color,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        error = {
                            Text(
                                text = character.name.take(2).uppercase(),
                                color = character.type.color,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = character.name,
                        color = TextParchment,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${character.type.label} • ${character.edition}",
                        color = character.type.color,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = character.ability,
                color = TextParchment,
                fontSize = 13.sp,
                lineHeight = 17.sp
            )

            // Night Wake Reminders
            if (character.firstNightReminder != null || character.otherNightsReminder != null) {
                HorizontalDivider(color = GrimoireBorder, modifier = Modifier.padding(vertical = 8.dp))
                if (character.firstNightReminder != null) {
                    Text(
                        text = "First Night: ${character.firstNightReminder}",
                        color = AntiqueGold,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
                if (character.otherNightsReminder != null) {
                    Text(
                        text = "Other Nights: ${character.otherNightsReminder}",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            if (character.defaultReminders.isNotEmpty()) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    character.defaultReminders.forEach { token ->
                        ReminderChip(text = token)
                    }
                }
            }
        }
    }
}

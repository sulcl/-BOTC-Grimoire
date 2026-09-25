package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GrimoireDarkColorScheme = darkColorScheme(
    primary = CrimsonBlood,
    onPrimary = Color.White,
    primaryContainer = CrimsonDeep,
    onPrimaryContainer = TextParchment,
    secondary = AntiqueGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF3E2C00),
    onSecondaryContainer = GoldGlow,
    tertiary = CrimsonGlow,
    onTertiary = Color.White,
    background = GrimoireFeltDark,
    onBackground = TextParchment,
    surface = GrimoireSurface,
    onSurface = TextParchment,
    surfaceVariant = GrimoireSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = GrimoireBorder,
    outlineVariant = Color(0xFF381B24),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GrimoireDarkColorScheme,
        typography = Typography,
        content = content
    )
}

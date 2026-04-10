package com.rjw.bunpoun3.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class ThemeMode {
    Light,
    Dark,
    System,
}

enum class ThemePreset {
    Classic,
    Ocean,
    Sakura,
    Forest,
    Sunset,
}

private data class ThemePalette(
    val light: ColorScheme,
    val dark: ColorScheme,
)

private val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.4).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.2).sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 24.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.25.sp,
    ),
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(18.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(30.dp),
    extraLarge = RoundedCornerShape(36.dp),
)

fun appColorSchemeFor(preset: ThemePreset, dark: Boolean): ColorScheme =
    if (dark) paletteFor(preset).dark else paletteFor(preset).light

@Composable
fun BunpouTheme(
    themeMode: ThemeMode = ThemeMode.Light,
    preset: ThemePreset = ThemePreset.Classic,
    content: @Composable () -> Unit,
) {
    val useDarkTheme = when (themeMode) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }

    MaterialTheme(
        colorScheme = appColorSchemeFor(preset = preset, dark = useDarkTheme),
        typography = AppTypography,
        shapes = AppShapes,
        content = content,
    )
}

private fun paletteFor(preset: ThemePreset): ThemePalette =
    when (preset) {
        ThemePreset.Classic -> classicPalette()
        ThemePreset.Ocean -> oceanPalette()
        ThemePreset.Sakura -> sakuraPalette()
        ThemePreset.Forest -> forestPalette()
        ThemePreset.Sunset -> sunsetPalette()
    }

private fun classicPalette(): ThemePalette = ThemePalette(
    light = lightColorScheme(
        primary = Color(0xFF1D3557),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFDCEAF2),
        onPrimaryContainer = Color(0xFF1D3557),
        secondary = Color(0xFF457B9D),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFEAF2F8),
        onSecondaryContainer = Color(0xFF1D3557),
        tertiary = Color(0xFFE63946),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFDE8DF),
        onTertiaryContainer = Color(0xFF732027),
        error = Color(0xFFE76F51),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFDE8DF),
        onErrorContainer = Color(0xFF6B2A1A),
        background = Color(0xFFF1FAEE),
        onBackground = Color(0xFF1D3557),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF1D3557),
        surfaceVariant = Color(0xFFF8F9FA),
        onSurfaceVariant = Color(0xFF6C757D),
        outline = Color(0xFFADB5BD),
        outlineVariant = Color(0xFFD9E0E5),
    ),
    dark = darkColorScheme(
        primary = Color(0xFFA8DADC),
        onPrimary = Color(0xFF10243E),
        primaryContainer = Color(0xFF243A5A),
        onPrimaryContainer = Color(0xFFE3F7F5),
        secondary = Color(0xFF89BEDD),
        onSecondary = Color(0xFF16324B),
        secondaryContainer = Color(0xFF274A67),
        onSecondaryContainer = Color(0xFFE8F4FB),
        tertiary = Color(0xFFFFA9A0),
        onTertiary = Color(0xFF5B1D22),
        tertiaryContainer = Color(0xFF7A2C34),
        onTertiaryContainer = Color(0xFFFFDAD6),
        error = Color(0xFFFFB59F),
        onError = Color(0xFF5B2113),
        errorContainer = Color(0xFF7D3421),
        onErrorContainer = Color(0xFFFFDAD1),
        background = Color(0xFF0F1724),
        onBackground = Color(0xFFE8EEF4),
        surface = Color(0xFF172033),
        onSurface = Color(0xFFE8EEF4),
        surfaceVariant = Color(0xFF253142),
        onSurfaceVariant = Color(0xFFB8C2CF),
        outline = Color(0xFF8191A3),
        outlineVariant = Color(0xFF344254),
    ),
)

private fun oceanPalette(): ThemePalette = ThemePalette(
    light = lightColorScheme(
        primary = Color(0xFF0C6C74),
        onPrimary = Color(0xFFF5FFFE),
        primaryContainer = Color(0xFFD7F3F1),
        onPrimaryContainer = Color(0xFF0A363C),
        secondary = Color(0xFF4F6F7B),
        onSecondary = Color(0xFFF8FAFC),
        secondaryContainer = Color(0xFFDCEBF3),
        onSecondaryContainer = Color(0xFF1F343E),
        tertiary = Color(0xFF8A5A3C),
        onTertiary = Color(0xFFFFF8F4),
        tertiaryContainer = Color(0xFFFFE1D0),
        onTertiaryContainer = Color(0xFF43200B),
        error = Color(0xFFB42318),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD5),
        onErrorContainer = Color(0xFF4A0D08),
        background = Color(0xFFF4F7FB),
        onBackground = Color(0xFF13212A),
        surface = Color(0xFFFBFCFE),
        onSurface = Color(0xFF13212A),
        surfaceVariant = Color(0xFFE6EDF2),
        onSurfaceVariant = Color(0xFF55636F),
        outline = Color(0xFF8493A0),
        outlineVariant = Color(0xFFD0D9E0),
    ),
    dark = darkColorScheme(
        primary = Color(0xFF78D7D1),
        onPrimary = Color(0xFF00373C),
        primaryContainer = Color(0xFF124D53),
        onPrimaryContainer = Color(0xFFD7F3F1),
        secondary = Color(0xFFBED0DB),
        onSecondary = Color(0xFF243942),
        secondaryContainer = Color(0xFF39505B),
        onSecondaryContainer = Color(0xFFDCEBF3),
        tertiary = Color(0xFFF2BB96),
        onTertiary = Color(0xFF4E280F),
        tertiaryContainer = Color(0xFF6B4125),
        onTertiaryContainer = Color(0xFFFFE1D0),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF0B141A),
        onBackground = Color(0xFFDFE9F2),
        surface = Color(0xFF101A21),
        onSurface = Color(0xFFDFE9F2),
        surfaceVariant = Color(0xFF24323C),
        onSurfaceVariant = Color(0xFFB6C6D2),
        outline = Color(0xFF8A9AA8),
        outlineVariant = Color(0xFF3A4954),
    ),
)

private fun sakuraPalette(): ThemePalette = ThemePalette(
    light = lightColorScheme(
        primary = Color(0xFF9D4D7B),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFBDDEA),
        onPrimaryContainer = Color(0xFF5D2044),
        secondary = Color(0xFF8C6A7A),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFF5DFE8),
        onSecondaryContainer = Color(0xFF452C38),
        tertiary = Color(0xFFB46B5C),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFDBD4),
        onTertiaryContainer = Color(0xFF4E241B),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        background = Color(0xFFFFF7FA),
        onBackground = Color(0xFF24161D),
        surface = Color(0xFFFFFBFF),
        onSurface = Color(0xFF24161D),
        surfaceVariant = Color(0xFFF1E0E8),
        onSurfaceVariant = Color(0xFF6E5962),
        outline = Color(0xFF9D8791),
        outlineVariant = Color(0xFFD8C2CB),
    ),
    dark = darkColorScheme(
        primary = Color(0xFFFFB1CF),
        onPrimary = Color(0xFF5E113F),
        primaryContainer = Color(0xFF7D3158),
        onPrimaryContainer = Color(0xFFFBDDEA),
        secondary = Color(0xFFDDBFCB),
        onSecondary = Color(0xFF3F2A33),
        secondaryContainer = Color(0xFF573F48),
        onSecondaryContainer = Color(0xFFF5DFE8),
        tertiary = Color(0xFFFFB4A5),
        onTertiary = Color(0xFF683C31),
        tertiaryContainer = Color(0xFF855446),
        onTertiaryContainer = Color(0xFFFFDBD4),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF1B1116),
        onBackground = Color(0xFFF2DEE7),
        surface = Color(0xFF22181D),
        onSurface = Color(0xFFF2DEE7),
        surfaceVariant = Color(0xFF51424A),
        onSurfaceVariant = Color(0xFFD5C1CA),
        outline = Color(0xFF9F8C95),
        outlineVariant = Color(0xFF51424A),
    ),
)

private fun forestPalette(): ThemePalette = ThemePalette(
    light = lightColorScheme(
        primary = Color(0xFF2D6A4F),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFD8F3DC),
        onPrimaryContainer = Color(0xFF163823),
        secondary = Color(0xFF5B7F67),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFDDECDD),
        onSecondaryContainer = Color(0xFF1E3525),
        tertiary = Color(0xFFE9C46A),
        onTertiary = Color(0xFF3E2F00),
        tertiaryContainer = Color(0xFFFFE7A8),
        onTertiaryContainer = Color(0xFF4D3A00),
        error = Color(0xFFB3261E),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFF9DEDC),
        onErrorContainer = Color(0xFF410E0B),
        background = Color(0xFFF6FBF5),
        onBackground = Color(0xFF142018),
        surface = Color(0xFFFCFEFA),
        onSurface = Color(0xFF142018),
        surfaceVariant = Color(0xFFE4ECE3),
        onSurfaceVariant = Color(0xFF58635A),
        outline = Color(0xFF879287),
        outlineVariant = Color(0xFFD0D9CF),
    ),
    dark = darkColorScheme(
        primary = Color(0xFF95D5B2),
        onPrimary = Color(0xFF123423),
        primaryContainer = Color(0xFF28553C),
        onPrimaryContainer = Color(0xFFD8F3DC),
        secondary = Color(0xFFB7D1BA),
        onSecondary = Color(0xFF263B2A),
        secondaryContainer = Color(0xFF3C5240),
        onSecondaryContainer = Color(0xFFDDECDD),
        tertiary = Color(0xFFFFD97D),
        onTertiary = Color(0xFF473500),
        tertiaryContainer = Color(0xFF665000),
        onTertiaryContainer = Color(0xFFFFE7A8),
        error = Color(0xFFF2B8B5),
        onError = Color(0xFF601410),
        errorContainer = Color(0xFF8C1D18),
        onErrorContainer = Color(0xFFF9DEDC),
        background = Color(0xFF0F1510),
        onBackground = Color(0xFFDDE5DA),
        surface = Color(0xFF151C16),
        onSurface = Color(0xFFDDE5DA),
        surfaceVariant = Color(0xFF404941),
        onSurfaceVariant = Color(0xFFBFC9BD),
        outline = Color(0xFF899388),
        outlineVariant = Color(0xFF404941),
    ),
)

private fun sunsetPalette(): ThemePalette = ThemePalette(
    light = lightColorScheme(
        primary = Color(0xFFB34B2F),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFDBD0),
        onPrimaryContainer = Color(0xFF482014),
        secondary = Color(0xFF8E5E3B),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFFDDBF),
        onSecondaryContainer = Color(0xFF352010),
        tertiary = Color(0xFF6B5CA5),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFE8DDFF),
        onTertiaryContainer = Color(0xFF251A53),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        background = Color(0xFFFFF8F3),
        onBackground = Color(0xFF231914),
        surface = Color(0xFFFFFBFF),
        onSurface = Color(0xFF231914),
        surfaceVariant = Color(0xFFF4DFD7),
        onSurfaceVariant = Color(0xFF6B5A54),
        outline = Color(0xFF9C8B85),
        outlineVariant = Color(0xFFD7C3BC),
    ),
    dark = darkColorScheme(
        primary = Color(0xFFFFB59C),
        onPrimary = Color(0xFF6A230D),
        primaryContainer = Color(0xFF8A3A1F),
        onPrimaryContainer = Color(0xFFFFDBD0),
        secondary = Color(0xFFFFB781),
        onSecondary = Color(0xFF512400),
        secondaryContainer = Color(0xFF6F3B11),
        onSecondaryContainer = Color(0xFFFFDDBF),
        tertiary = Color(0xFFCFBFFF),
        onTertiary = Color(0xFF392B70),
        tertiaryContainer = Color(0xFF504289),
        onTertiaryContainer = Color(0xFFE8DDFF),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF17110E),
        onBackground = Color(0xFFEFE0DA),
        surface = Color(0xFF1F1915),
        onSurface = Color(0xFFEFE0DA),
        surfaceVariant = Color(0xFF52443E),
        onSurfaceVariant = Color(0xFFD8C2BA),
        outline = Color(0xFFA18C84),
        outlineVariant = Color(0xFF52443E),
    ),
)

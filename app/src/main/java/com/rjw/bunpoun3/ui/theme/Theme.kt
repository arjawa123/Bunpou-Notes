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
        primary = Color(0xFF20324A),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFDDE7F0),
        onPrimaryContainer = Color(0xFF152235),
        secondary = Color(0xFF42697C),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE3EEF2),
        onSecondaryContainer = Color(0xFF19313D),
        tertiary = Color(0xFFD53F4D),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFF8DDE0),
        onTertiaryContainer = Color(0xFF5E1821),
        error = Color(0xFFB3261E),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        background = Color(0xFFF4F8F1),
        onBackground = Color(0xFF172333),
        surface = Color(0xFFFFFCF8),
        onSurface = Color(0xFF172333),
        surfaceVariant = Color(0xFFE7ECE8),
        onSurfaceVariant = Color(0xFF5D6770),
        outline = Color(0xFF8B96A0),
        outlineVariant = Color(0xFFD4DCE1),
    ),
    dark = darkColorScheme(
        primary = Color(0xFFB8C9DE),
        onPrimary = Color(0xFF1A2B40),
        primaryContainer = Color(0xFF2A3B52),
        onPrimaryContainer = Color(0xFFE1ECF7),
        secondary = Color(0xFFA9C7D5),
        onSecondary = Color(0xFF17313E),
        secondaryContainer = Color(0xFF294553),
        onSecondaryContainer = Color(0xFFE1F0F6),
        tertiary = Color(0xFFFFB3BA),
        onTertiary = Color(0xFF601923),
        tertiaryContainer = Color(0xFF7D2A35),
        onTertiaryContainer = Color(0xFFFFDDE0),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF101822),
        onBackground = Color(0xFFE5EBF1),
        surface = Color(0xFF18212C),
        onSurface = Color(0xFFE5EBF1),
        surfaceVariant = Color(0xFF303A45),
        onSurfaceVariant = Color(0xFFC4CED8),
        outline = Color(0xFF8F9BA7),
        outlineVariant = Color(0xFF424D58),
    ),
)

private fun oceanPalette(): ThemePalette = ThemePalette(
    light = lightColorScheme(
        primary = Color(0xFF0E6570),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFD7EEF1),
        onPrimaryContainer = Color(0xFF073238),
        secondary = Color(0xFF3D6B7D),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFDCEBF2),
        onSecondaryContainer = Color(0xFF173543),
        tertiary = Color(0xFFD33D4C),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFF8DDE1),
        onTertiaryContainer = Color(0xFF5E1821),
        error = Color(0xFFB42318),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD5),
        onErrorContainer = Color(0xFF4A0D08),
        background = Color(0xFFF3F8FA),
        onBackground = Color(0xFF14242C),
        surface = Color(0xFFFFFCF8),
        onSurface = Color(0xFF14242C),
        surfaceVariant = Color(0xFFE1EBEF),
        onSurfaceVariant = Color(0xFF53636C),
        outline = Color(0xFF85949C),
        outlineVariant = Color(0xFFD0DCE1),
    ),
    dark = darkColorScheme(
        primary = Color(0xFF83D7DE),
        onPrimary = Color(0xFF00363D),
        primaryContainer = Color(0xFF15515A),
        onPrimaryContainer = Color(0xFFD7EEF1),
        secondary = Color(0xFFB6D2DE),
        onSecondary = Color(0xFF1E3A48),
        secondaryContainer = Color(0xFF334F5D),
        onSecondaryContainer = Color(0xFFDCEBF2),
        tertiary = Color(0xFFFFB3BA),
        onTertiary = Color(0xFF601923),
        tertiaryContainer = Color(0xFF7D2A35),
        onTertiaryContainer = Color(0xFFFFDDE0),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF0C171C),
        onBackground = Color(0xFFE1EBEF),
        surface = Color(0xFF121E24),
        onSurface = Color(0xFFE1EBEF),
        surfaceVariant = Color(0xFF2A3A42),
        onSurfaceVariant = Color(0xFFC1CED6),
        outline = Color(0xFF8B9AA2),
        outlineVariant = Color(0xFF405059),
    ),
)

private fun sakuraPalette(): ThemePalette = ThemePalette(
    light = lightColorScheme(
        primary = Color(0xFF8B4A6F),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFF4DDE8),
        onPrimaryContainer = Color(0xFF4B2338),
        secondary = Color(0xFF77606A),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFEDE0E5),
        onSecondaryContainer = Color(0xFF352A30),
        tertiary = Color(0xFFD34455),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFF9DDE2),
        onTertiaryContainer = Color(0xFF5D1824),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        background = Color(0xFFFFF7F9),
        onBackground = Color(0xFF24161D),
        surface = Color(0xFFFFFCF8),
        onSurface = Color(0xFF24161D),
        surfaceVariant = Color(0xFFEEE2E7),
        onSurfaceVariant = Color(0xFF6E5962),
        outline = Color(0xFF9D8791),
        outlineVariant = Color(0xFFD8C8D0),
    ),
    dark = darkColorScheme(
        primary = Color(0xFFF2B6D2),
        onPrimary = Color(0xFF4B2338),
        primaryContainer = Color(0xFF6D3A56),
        onPrimaryContainer = Color(0xFFF4DDE8),
        secondary = Color(0xFFD9C4CE),
        onSecondary = Color(0xFF392B32),
        secondaryContainer = Color(0xFF51434A),
        onSecondaryContainer = Color(0xFFEDE0E5),
        tertiary = Color(0xFFFFB3BA),
        onTertiary = Color(0xFF601923),
        tertiaryContainer = Color(0xFF7D2A35),
        onTertiaryContainer = Color(0xFFFFDDE0),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF1B1116),
        onBackground = Color(0xFFF2DEE7),
        surface = Color(0xFF241A20),
        onSurface = Color(0xFFF2DEE7),
        surfaceVariant = Color(0xFF493D44),
        onSurfaceVariant = Color(0xFFD5C1CA),
        outline = Color(0xFF9F8C95),
        outlineVariant = Color(0xFF5B4D55),
    ),
)

private fun forestPalette(): ThemePalette = ThemePalette(
    light = lightColorScheme(
        primary = Color(0xFF2F6B52),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFDDEFE3),
        onPrimaryContainer = Color(0xFF153825),
        secondary = Color(0xFF5D7865),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE0E9DF),
        onSecondaryContainer = Color(0xFF26382A),
        tertiary = Color(0xFFD1404E),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFF8DDE0),
        onTertiaryContainer = Color(0xFF5D1821),
        error = Color(0xFFB3261E),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFF9DEDC),
        onErrorContainer = Color(0xFF410E0B),
        background = Color(0xFFF5FAF3),
        onBackground = Color(0xFF142018),
        surface = Color(0xFFFFFCF8),
        onSurface = Color(0xFF142018),
        surfaceVariant = Color(0xFFE4ECE2),
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
        tertiary = Color(0xFFFFB3BA),
        onTertiary = Color(0xFF601923),
        tertiaryContainer = Color(0xFF7D2A35),
        onTertiaryContainer = Color(0xFFFFDDE0),
        error = Color(0xFFF2B8B5),
        onError = Color(0xFF601410),
        errorContainer = Color(0xFF8C1D18),
        onErrorContainer = Color(0xFFF9DEDC),
        background = Color(0xFF0F1510),
        onBackground = Color(0xFFDDE5DA),
        surface = Color(0xFF171E18),
        onSurface = Color(0xFFDDE5DA),
        surfaceVariant = Color(0xFF354037),
        onSurfaceVariant = Color(0xFFBFC9BD),
        outline = Color(0xFF899388),
        outlineVariant = Color(0xFF475148),
    ),
)

private fun sunsetPalette(): ThemePalette = ThemePalette(
    light = lightColorScheme(
        primary = Color(0xFF9D5B3B),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFF5DECE),
        onPrimaryContainer = Color(0xFF452211),
        secondary = Color(0xFF80684E),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFEDE3D7),
        onSecondaryContainer = Color(0xFF362A1F),
        tertiary = Color(0xFFD0434E),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFF8DDE0),
        onTertiaryContainer = Color(0xFF5D1821),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        background = Color(0xFFFFF7F0),
        onBackground = Color(0xFF231914),
        surface = Color(0xFFFFFCF8),
        onSurface = Color(0xFF231914),
        surfaceVariant = Color(0xFFEDE3DC),
        onSurfaceVariant = Color(0xFF6B5A54),
        outline = Color(0xFF9C8B85),
        outlineVariant = Color(0xFFD8C8C0),
    ),
    dark = darkColorScheme(
        primary = Color(0xFFE9B896),
        onPrimary = Color(0xFF4F2A16),
        primaryContainer = Color(0xFF70452B),
        onPrimaryContainer = Color(0xFFF5DECE),
        secondary = Color(0xFFD8C3AD),
        onSecondary = Color(0xFF3B2D20),
        secondaryContainer = Color(0xFF544536),
        onSecondaryContainer = Color(0xFFEDE3D7),
        tertiary = Color(0xFFFFB3BA),
        onTertiary = Color(0xFF601923),
        tertiaryContainer = Color(0xFF7D2A35),
        onTertiaryContainer = Color(0xFFFFDDE0),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF17110E),
        onBackground = Color(0xFFEFE0DA),
        surface = Color(0xFF211A16),
        onSurface = Color(0xFFEFE0DA),
        surfaceVariant = Color(0xFF493D36),
        onSurfaceVariant = Color(0xFFD8C2BA),
        outline = Color(0xFFA18C84),
        outlineVariant = Color(0xFF5B4D45),
    ),
)

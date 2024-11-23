package ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color.Blue,
    secondary = Color.Black,
    tertiary = Color.Magenta
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Blue,
    secondary = Color.White,
    tertiary = Color.Yellow

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
object Theme {
    val Dark = darkColorScheme(
        primary = Color(0xFF1E88E5),
        onPrimary = Color.White,
        background = Color(0xFF121212),
        onBackground = Color.White,
        secondary = Color(0xFF90CAF9),
        onSecondary = Color.White
    )

    val Light = lightColorScheme(
        primary = Color(0xFF1E88E5),
        onPrimary = Color.White,
        background = Color(0xFFFFFFFF),
        onBackground = Color.Black,
        secondary = Color(0xFF90CAF9),
        onSecondary = Color.White
    )
//    val Light = lightColorScheme(
//        background = Colors.container_light,
//        onBackground = Colors.text_light,
//        primaryContainer = Colors.black,
//        onPrimaryContainer = Colors.container_light,
//        secondaryContainer = Colors.purple_500,
//        onSecondaryContainer = Colors.purple_200
//    )
//    val Dark = darkColorScheme(
//        background = Colors.background_dark,
//        onBackground = Colors.text_dark,
//        primaryContainer = Colors.text_dark,
//        onPrimaryContainer = Colors.container_dark
//    )
val lightTritanopia = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color.White,
    background = Colors.background_light_tritanopia,
    onBackground = Color.Black,
    secondary = Color(0xFF64B5F6),
    onSecondary = Color.White
)
//    val lightTritanopia = lightColorScheme(
//        background = Colors.background_light_tritanopia,
//        onBackground = Colors.text_light_tritanopia,
//        primaryContainer = Colors.text_light_tritanopia,
//        onPrimaryContainer = Colors.background_light_tritanopia
//    )
val DarkTritanopia = darkColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color.White,
    background = Colors.background_dark_tritanopia,
    onBackground = Color.White,
    secondary = Color(0xFF64B5F6),
    onSecondary = Color.White
)
//    val DarkTritanopia = darkColorScheme(
//        background = Colors.background_dark_tritanopia,
//        onBackground = Colors.text_dark_tritanopia,
//        primaryContainer = Colors.text_dark_tritanopia,
//        onPrimaryContainer = Colors.background_dark_tritanopia
//    )
val DarkProtanopia = darkColorScheme(
    primary = Color(0xFF1E88E5),
    onPrimary = Color.White,
    background = Colors.background_dark_protanopia,
    onBackground = Color.White,
    secondary = Color(0xFF90CAF9),
    onSecondary = Color.White
)
    val lightProtanopia = lightColorScheme(
        primary = Color(0xFF1565C0),
        onPrimary = Color.White,
        background = Colors.background_light_protanopia,
        onBackground = Color.Black,
        secondary = Color(0xFF64B5F6),
        onSecondary = Color.White
    )
//    val lightProtanopia = lightColorScheme(
//        background = Colors.background_light_protanopia,
//        onBackground = Colors.text_light_protanopia,
//        primaryContainer = Colors.text_light_protanopia,
//        onPrimaryContainer = Colors.background_light_protanopia
//    )
//    val DarkProtanopia = darkColorScheme(
//        background = Colors.background_dark_protanopia,
//        onBackground = Colors.text_dark_protanopia,
//        primaryContainer = Colors.text_dark_protanopia,
//        onPrimaryContainer = Colors.background_dark_protanopia
//    )
val lightDeuteranopia = lightColorScheme(
    primary = Color(0xFF1E88E5),
    onPrimary = Color.White,
    background = Colors.background_light_deuteranopia,
    onBackground = Color.Black,
    secondary = Color(0xFF90CAF9),
    onSecondary = Color.White
)
//    val lightDeuteranopia = lightColorScheme(
//        background = Colors.background_light_deuteranopia,
//        onBackground = Colors.text_light_deuteranopia,
//        primaryContainer = Colors.text_light_deuteranopia,
//        onPrimaryContainer = Colors.background_light_deuteranopia
//    )
val DarkDeuteranopia = darkColorScheme(
    primary = Color(0xFF1E88E5),
    onPrimary = Color.White,
    background = Colors.background_dark_deuteranopia,
    onBackground = Color.White,
    secondary = Color(0xFF90CAF9),
    onSecondary = Color.White
)
//    val DarkDeuteranopia = darkColorScheme(
//        background = Colors.background_dark_deuteranopia,
//        onBackground = Colors.text_dark_deuteranopia,
//        primaryContainer = Colors.text_dark_deuteranopia,
//        onPrimaryContainer = Colors.background_dark_deuteranopia
//    )
}

@Composable
fun JobderTheme(
    colorScheme: ColorScheme,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
        typography = Typography
    )
}
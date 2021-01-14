package leancher.android.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

private val Yellow200 = Color(0xffffeb46)
private val Blue200 = Color(0xff91a4fc)

val White = Color.White
val Gray = Color.Gray

val DarkColors = darkColors(
        primary = Yellow200,
        onPrimary = Color.Blue,
        primaryVariant = Color.Cyan,

        secondary = Blue200,
        onSecondary = Color.Red,

        background = Color.DarkGray,
        onBackground = Color.LightGray,

        surface = Color.Green,
        onSurface = Color.Magenta
)

val LightColors = lightColors(
        primary = Yellow200,
        onPrimary = Color.Blue,
        primaryVariant = Color.Cyan,

        secondary = Blue200,
        onSecondary = Color.Red,
        secondaryVariant = Color.Magenta,

        background = Color.LightGray,
        onBackground = Color.DarkGray,

        surface = Color.Green,
        onSurface = Color.Magenta

)
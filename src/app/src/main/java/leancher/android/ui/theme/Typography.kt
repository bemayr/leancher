package leancher.android.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.sp
import leancher.android.R

// Example: define custom fonts
// val Rubik = fontFamily(
//        font(R.font.rubik_regular),
//        font(R.font.rubik_medium, FontWeight.W500),
//        font(R.font.rubik_bold, FontWeight.Bold)
// )

val LeancherTypography = Typography (
        h1 = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W300,
                fontSize = 25.sp
        ),
        body1 = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W600,
                fontSize = 15.sp
        )
)
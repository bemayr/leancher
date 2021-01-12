package leancher.android.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun StandardText(): TextStyle {
    return TextStyle(
        color = White()
    )
}

fun Heading(): TextStyle {
    return TextStyle(
        color = White(),
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold
    )
}
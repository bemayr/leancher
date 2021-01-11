package leancher.android.ui.pages

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import leancher.android.ui.theme.White

@Composable
fun Home(page: Int) {
    Text(text = "Lenacher Home, Page: $page", color = White())
}
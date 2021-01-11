package leancher.android.ui.pages

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import leancher.android.ui.theme.White

@Composable
fun Feed(page: Int) {
    Text(text = "Feed, Page: $page", color = White())
}
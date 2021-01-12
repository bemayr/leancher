package leancher.android.ui.pages

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import leancher.android.ui.theme.White

@Composable
fun Home(page: Int, launchIntent: () -> Unit) {
    Text(text = "Lenacher Home, Page: $page", color = White())
    IntentButton(name = "Test Intent", launchIntent = launchIntent)
}

@Composable
fun IntentButton(name: String, launchIntent: () -> Unit) {
    Button(onClick = {
        launchIntent()
    }) {
        Text(text = "Launch $name!")
    }
}
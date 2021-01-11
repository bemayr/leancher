package leancher.android.ui.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun TestButton(text: String, action: () -> Unit) {
    Button(onClick = {
        action()
    }) {
        Text(text = "$text!")
    }
}
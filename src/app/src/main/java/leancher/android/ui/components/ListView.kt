package leancher.android.ui.components

import androidx.compose.runtime.Composable
import androidx.ui.core.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.Column

@Composable
fun <T> ListView(list: List<T>) {
    VerticalScroller {
        Column {
            list.forEach { e ->
                Text(
                    text = "${e}!"
                )
            }
        }
    }
}
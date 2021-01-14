package leancher.android.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.Switch
import leancher.android.ui.theme.White
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ActionSwitch(text: String, onAction: () -> Unit, offAction: () -> Unit) {
    val checkedState = remember { mutableStateOf(false) }
    Row(Modifier.wrapContentSize(Alignment.Center)) {
        Column {
            Text(text = text, color = White())
        }
        Column {
            Switch(checked = checkedState.value, onCheckedChange = { checked ->
                checkedState.value = checked
                if(checkedState.value) {
                    onAction()
                } else {
                    offAction()
                }
            })
        }
    }
}
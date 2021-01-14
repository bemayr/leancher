package leancher.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.Switch
import leancher.android.ui.theme.White
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActionSwitch(text: String, onAction: () -> Unit, offAction: () -> Unit) {
    val checkedState = remember { mutableStateOf(false) }
    Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = text)
        Spacer(modifier = Modifier.width(15.dp))
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
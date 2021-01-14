package leancher.android.ui.components

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import leancher.android.ui.theme.White

@Composable
fun NotificationList(notifications: List<String>) {
    ScrollableColumn(Modifier.fillMaxHeight()) {
        notifications.forEach { notification ->
            if (notifications.indexOf(notification) == 0) {
                    Text(
                            modifier = Modifier.padding(15.dp),
                            text = "All notifications",
                            style = MaterialTheme.typography.h1
                    )
            }
            ListItem {
                Text(text = notification)
            }
            Divider(color = White)
        }
    }
}
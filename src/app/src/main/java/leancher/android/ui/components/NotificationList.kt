package leancher.android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import leancher.android.domain.models.Notification
import leancher.android.ui.theme.White

@Composable
fun NotificationList(notifications: List<Notification>?) {
    ScrollableColumn(Modifier.fillMaxHeight()) {
        if(notifications == null || (notifications != null && notifications.size == 0)) {
            Text(text = "No active notifications!")
        } else {
            notifications.forEach { notification ->
                if (notifications.indexOf(notification) == 0) {
                    Text(
                        modifier = Modifier.padding(15.dp),
                        text = "All notifications",
                        style = MaterialTheme.typography.h1
                    )
                }
                ListItem {
                    Column() {
                        Text(text = "image")
//                        Image(imageResource(id = notification.iconId),
//                            contentScale =  ContentScale.Crop,
//                            modifier = Modifier.fillMaxSize())
                    }
                    Column() {
                        Row() {
                            Text(text = notification.title, style = MaterialTheme.typography.h4)
                        }
                        Row() {
                            Text(text = notification.text)
                        }
                    }
                }
                Divider(color = White)
            }   
        }
    }
}
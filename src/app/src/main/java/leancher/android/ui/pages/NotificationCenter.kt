package leancher.android.ui.pages

import android.app.NotificationManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import leancher.android.R
import leancher.android.domain.models.PageTitle
import leancher.android.ui.components.ActionButton
import leancher.android.ui.components.TitleCard
import leancher.android.ui.theme.White

var notificationTitleModel = PageTitle("Notification Center", "Manage your notifications here", R.drawable.notification)

@Composable
fun NotificationCenter(page: Int) {

    val context = ContextAmbient.current

    val fakeNotifications = listOf<String>("1", "2", "3", "4", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5")

    Row {
        Column(Modifier.padding(20.dp)) {
            TitleCard(pageTitle = notificationTitleModel, null)
        }
    }

    // ActionButton(text = "Print Notification", action = {
        // val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // val myNotificationService: NotificationService? = context.getSystemService(NotificationService::class.java)
        // val notifications = NotificationService().getActiveNotifications()
    // }

    Text(text = "NotificationCenter, Page: $page", color = White())
    ActionButton(text = "Print Notification", action = {
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifications = notificationManager.activeNotifications

        println("notifications length ${notifications?.size}")
        notifications?.forEach { n -> println(n.notification) }
    })
}
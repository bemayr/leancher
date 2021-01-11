package leancher.android.ui.pages

import android.app.NotificationManager
import android.content.Context
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ContextAmbient
import leancher.android.ui.components.ListView
import leancher.android.ui.components.TestButton
import leancher.android.ui.theme.White

@Composable
fun NotificationCenter(page: Int) {

    val context = ContextAmbient.current

    val fakeNotifications = listOf<String>("1", "2", "3", "4", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5")
    
    Text(text = "NotificationCenter, Page: $page", color = White())
    TestButton(text = "Print Notification", action = {
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifications = notificationManager.activeNotifications

        println("notifications length ${notifications.size}")
        notifications.forEach { n -> println(n.notification) }
    })
}
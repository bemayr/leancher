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
import leancher.android.ui.components.ActionSwitch
import leancher.android.ui.components.IconButton
import leancher.android.ui.components.TitleCard
import leancher.android.ui.theme.White

lateinit var notificationTitleModel: PageTitle

@Composable
fun NotificationCenter(page: Int) {
    val context = ContextAmbient.current

    notificationTitleModel = PageTitle(
            context.getString(leancher.android.R.string.page_notification_center),
            "Manage your notifications here",
            R.drawable.notification)

    val fakeNotifications = listOf<String>("1", "2", "3", "4", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5", "5")

    Row {
        Column(Modifier.padding(10.dp)) {
            TitleCard(pageTitle = notificationTitleModel, null)
        }
    }

    Row {
        Column(Modifier.padding(10.dp)) {
            ActionSwitch(
                    text = "Enable / Disable Notifications",
                    onAction = { println(" ============= ON ============= ") },
                    offAction = { println(" ============= OFF ============= ") })
        }
    }

    Row {
        IconButton(icon = R.drawable.notification, action = { println(" ==== DELETE ====") })
    }

    // ActionButton(text = "Print Notification", action = {
        // val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // val myNotificationService: NotificationService? = context.getSystemService(NotificationService::class.java)
        // val notifications = NotificationService().getActiveNotifications()
    // }

    ActionButton(text = "Print Notification", action = {
        
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifications = notificationManager.activeNotifications

        println("notifications length ${notifications?.size}")
        notifications?.forEach { n -> println(n.notification) }
    })
}

fun hideStatusBar() {
    // Hide the status bar.
    // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

    // Remember that you should never show the action bar if the
    // status bar is hidden, so hide that too if necessary.
    // actionBar?.hide()
}
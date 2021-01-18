package leancher.android.ui.pages

import android.app.NotificationManager
import android.content.Context
import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import leancher.android.MainActivity
import leancher.android.R
import leancher.android.domain.models.PageTitle
import leancher.android.ui.components.*
import leancher.android.viewmodels.NotificationCenterViewModel

@Composable
fun NotificationCenter(notificationCenterViewModel: NotificationCenterViewModel) {
    val context = AmbientContext.current
    val activity: MainActivity = context as MainActivity

    val notificationTitleModel = PageTitle(
            context.getString(leancher.android.R.string.page_notification_center),
            "Manage your notifications here",
            R.drawable.notification)

    Row {
        Column(Modifier.padding(10.dp)) {
            TitleCard(pageTitle = notificationTitleModel, null)
        }
    }

    Row {
        Column(Modifier.padding(10.dp)) {
            ActionSwitch(
                    onAction = { println(" ============= ON ============= ") },
                    offAction = { println(" ============= OFF ============= ") },
                    text = "Enable / Disable Notifications")
        }
    }

    Row {
        ActionDialogDemo()

        IconButton(icon = Icons.Filled.Delete, action = {
            activity.clearNotifications()
        }, "Clear all")

        ActionButton(text = "Print Notification", action = {
            activity.readNotifications()
        })
    }
    
    Row {
        NotificationList(notifications = notificationCenterViewModel.notifications)
    }
}

fun hideStatusBar() {
    // Hide the status bar.
    // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

    // Remember that you should never show the action bar if the
    // status bar is hidden, so hide that too if necessary.
    // actionBar?.hide()
}
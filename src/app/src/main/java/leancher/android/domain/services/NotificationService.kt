package leancher.android.domain.services

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import java.util.concurrent.Semaphore

// https://gist.github.com/paulo-raca/471680c0fe4d8f91b8cde486039b0dcd
// https://www.javacodegeeks.com/2013/10/android-notificationlistenerservice-example.html
// https://github.com/Chagall/notification-listener-service-example

class NotificationService: NotificationListenerService() {
    private val TAG = "NotificationService"

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun getActiveNotifications(): Array<StatusBarNotification> {
        return super.getActiveNotifications()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        println("notification received")
        println(sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}

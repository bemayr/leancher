package leancher.android.domain.services

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson


// https://gist.github.com/paulo-raca/471680c0fe4d8f91b8cde486039b0dcd
// https://www.javacodegeeks.com/2013/10/android-notificationlistenerservice-example.html
// https://github.com/Chagall/notification-listener-service-example
// https://github.com/hiteshsahu/Android-Notification-Demo

class NotificationService : NotificationListenerService() {

    private lateinit var gson: Gson
    private var commandFromUIReceiver: CommandFromUIReceiver? = null

    override fun onCreate() {
        super.onCreate()

        // Register broadcast from UI
        commandFromUIReceiver = CommandFromUIReceiver()
        val filter = IntentFilter()
        filter.addAction(READ_COMMAND_ACTION)
        registerReceiver(commandFromUIReceiver, filter)

        gson = Gson()

        fetchCurrentNotifications()
    }


    /**
     * New Notn Added Callback
     */
    override fun onNotificationPosted(newNotification: StatusBarNotification) {
        Log.i(
            TAG,
            "-------- onNotificationPosted(): " + "ID :" + newNotification.id + "\t" + newNotification.notification.tickerText + "\t" + newNotification.packageName
        )
        sendResultOnUI("onNotificationPosted :" + newNotification.packageName + "\n")
    }

    /**
     * Notn Removed callback
     */
    override fun onNotificationRemoved(removedNotification: StatusBarNotification) {
        Log.i(
            TAG,
            "-------- onNotificationRemoved() :" + "ID :" + removedNotification.id + "\t" + removedNotification.notification.tickerText + "\t" + removedNotification.packageName
        )
        sendResultOnUI("onNotificationRemoved: " + removedNotification.packageName + "\n")
    }


    internal inner class CommandFromUIReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getStringExtra(COMMAND_KEY) == CLEAR_NOTIFICATIONS)
                // remove Notifications
                cancelAllNotifications()
            else if (intent.getStringExtra(COMMAND_KEY) == GET_ACTIVE_NOTIFICATIONS)
                // read Notifications
                fetchCurrentNotifications()
        }
    }


    /**
     * Fetch list of Active Notifications
     */
    private fun fetchCurrentNotifications() {
        var notifications = mutableListOf<leancher.android.domain.models.Notification>()
        this@NotificationService.activeNotifications.forEach { statusBarNotification ->
            val extras: Bundle = statusBarNotification.notification.extras
            val notification = leancher.android.domain.models.Notification (
                        key = statusBarNotification.key,
                        title = extras[Notification.EXTRA_TITLE].toString(),
                        text = extras[Notification.EXTRA_TEXT].toString(),
                        iconId = statusBarNotification.notification.icon,
                        icon = statusBarNotification.notification.smallIcon,
                        originalNotificaion = statusBarNotification
                    )
            notifications.add(notification)
        }

        sendResultOnUI(gson.toJson(notifications))
    }


    // sendMessage success result on UI
    private fun sendResultOnUI(result: String?) {
        val resultIntent = Intent(UPDATE_UI_ACTION)
        resultIntent.putExtra(RESULT_KEY, Activity.RESULT_OK)
        resultIntent.putExtra(RESULT_VALUE, result)
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(commandFromUIReceiver)
    }

    companion object {
        const val TAG = "NotificationListener"

        //Update UI action
        const val UPDATE_UI_ACTION =   "ACTION_UPDATE_UI"
        const val READ_COMMAND_ACTION = "ACTION_READ_COMMAND"


        // Bundle Key Value Pair
        const val RESULT_KEY = "readResultKey"
        const val RESULT_VALUE = "readResultValue"


        //Actions sent from UI
        const val COMMAND_KEY = "READ_COMMAND"
        const val CLEAR_NOTIFICATIONS = "clearall"
        const val GET_ACTIVE_NOTIFICATIONS = "list"
    }
}

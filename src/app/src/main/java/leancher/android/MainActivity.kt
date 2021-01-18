package leancher.android

import android.Manifest
import android.app.NotificationManager
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import leancher.android.domain.models.Notification
import leancher.android.domain.services.NotificationService
import leancher.android.domain.services.NotificationService.Companion.CLEAR_NOTIFICATIONS
import leancher.android.domain.services.NotificationService.Companion.COMMAND_KEY
import leancher.android.domain.services.NotificationService.Companion.DISMISS_NOTIFICATION
import leancher.android.domain.services.NotificationService.Companion.GET_ACTIVE_NOTIFICATIONS
import leancher.android.domain.services.NotificationService.Companion.READ_COMMAND_ACTION
import leancher.android.domain.services.NotificationService.Companion.RESULT_KEY
import leancher.android.domain.services.NotificationService.Companion.RESULT_VALUE
import leancher.android.domain.services.NotificationService.Companion.UPDATE_UI_ACTION
import leancher.android.ui.layouts.PagerLayout
import leancher.android.ui.theme.LeancherTheme
import leancher.android.viewmodels.*
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS =
        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

    private val APPWIDGET_HOST_ID = 1024
    private val REQUEST_CREATE_APPWIDGET = 5
    private val REQUEST_PICK_APPWIDGET = 9

    private lateinit var appWidgetManager: AppWidgetManager
    private lateinit var appWidgetHost: AppWidgetHost

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var viewModelStateManager: ViewModelStateManager
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("com.Leancher", MODE_PRIVATE);

        appWidgetManager = AppWidgetManager.getInstance(this)
        appWidgetHost = AppWidgetHost(this, APPWIDGET_HOST_ID)

        requestLeancherPermissions()

        viewModelStateManager = ViewModelStateManager(this)
        initializeViewState()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
//        notificationManager.notificationPolicy = NotificationManager.Policy(
//            NotificationManager.Policy.PRIORITY_CATEGORY_CALLS,
//            NotificationManager.Policy.CONVERSATION_SENDERS_ANYONE,
//            NotificationManager.Policy.PRIORITY_SENDERS_ANY
//        )

        // set default view with compose => Pager
        setContent {
            Leancher()
        }
    }

    // Define the callback for what to do when data is received
    private val clientLooperReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val resultCode = intent.getIntExtra(RESULT_KEY, RESULT_CANCELED)
            if (resultCode == RESULT_OK) {
                val resultValue = intent.getStringExtra(RESULT_VALUE)
                val gson = Gson()
                val type: Type = object : TypeToken<MutableList<Notification>>() {}.type

                val notifications: List<Notification> =
                    gson.fromJson(resultValue, type) as List<Notification>
                mainActivityViewModel.notificationCenterViewModel.notifications = mutableListOf()
                notifications.forEach { notification ->
                    mainActivityViewModel.notificationCenterViewModel.notifications!!.add(
                        notification
                    )
                }
            }
        }
    }

    private fun initializeViewState() {
        val viewState = viewModelStateManager.restoreViewState()
        if (viewState != null) {
            mainActivityViewModel = viewState
        } else {
            mainActivityViewModel = MainActivityViewModel(
                homeViewModel = HomeViewModel(),
                feedViewModel = FeedViewModel(
                    widgets = mutableListOf()
                ),
                notificationCenterViewModel = NotificationCenterViewModel(
                    notifications = mutableListOf()
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()
        appWidgetHost.startListening()
    }

    override fun onResume() {
        super.onResume()

        if (sharedPreferences.getBoolean("firstRun", true)) {
            editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false)
            editor.commit();

            requestLeancherPermissions()
        }

        //Register to Broadcast for Updating UI
        LocalBroadcastManager.getInstance(this).registerReceiver(
            clientLooperReceiver,
            IntentFilter(UPDATE_UI_ACTION)
        )

        readNotifications()
    }

    override fun onPause() {
        super.onPause()
        viewModelStateManager.persistViewState(mainActivityViewModel)
    }

    override fun onStop() {
        super.onStop()
        appWidgetHost.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()

        //Unregister to Broadcast for Updating UI
        LocalBroadcastManager.getInstance(this).unregisterReceiver(clientLooperReceiver)
    }

    private fun requestLeancherPermissions() {
        if (checkSelfPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY), 1);
        }
        if (!isNotificationServiceEnabled()) {
            startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }

    fun readNotifications() {
        val i = Intent(READ_COMMAND_ACTION)
        i.putExtra(COMMAND_KEY, GET_ACTIVE_NOTIFICATIONS)
        sendBroadcast(i)
    }

    fun clearNotifications() {
        val i = Intent(READ_COMMAND_ACTION)
        i.putExtra(COMMAND_KEY, CLEAR_NOTIFICATIONS)
        sendBroadcast(i)
    }

    fun dismissNotification(key: String) {
        val i = Intent(READ_COMMAND_ACTION)
        i.putExtra(COMMAND_KEY, DISMISS_NOTIFICATION)
        i.putExtra(RESULT_KEY, RESULT_OK)
        i.putExtra(RESULT_VALUE, key)
        sendBroadcast(i)
    }

    private fun getNotifications() {
        Log.i(TAG, "Waiting for MyNotificationService")
        val myNotificationService: NotificationService? =
            getSystemService(NotificationService::class.java)
        Log.i(TAG, "Active Notifications: [")
        if (myNotificationService != null) {
            for (notification in myNotificationService.getActiveNotifications()) {
                Log.i(TAG, "    " + notification.packageName + " / " + notification.tag)
            }
        }
        Log.i(TAG, "]")
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val allNames =
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if (allNames != null && !allNames.isEmpty()) {
            for (name in allNames.split(":").toTypedArray()) {
                if (packageName == ComponentName.unflattenFromString(name)!!.packageName) {
                    return true
                }
            }
        }
        return false
    }

    private fun launchIntentTest() {
        val uriString = "https://stackoverflow.com/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uriString)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_APPWIDGET) {
                configureWidget(data)
            } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                if (data != null) {
                    createWidget(data)
                }
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {
            val appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
            if (appWidgetId != -1) {
                appWidgetHost.deleteAppWidgetId(appWidgetId)
            }
        }
    }

    fun selectWidget() {
        val appWidgetId = appWidgetHost.allocateAppWidgetId()
        val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET)
    }

    private fun configureWidget(data: Intent?) {
        val extras = data!!.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId)
        if (appWidgetInfo.configure != null) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
            intent.component = appWidgetInfo.configure
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET)
        } else {
            createWidget(data)
        }
    }

    private fun createWidget(data: Intent) {
        val extras = data.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = appWidgetManager.getAppWidgetInfo(appWidgetId)
        // val hostView = appWidgetHost.createView(this, appWidgetId, appWidgetInfo)
        // hostView.setAppWidget(appWidgetId, appWidgetInfo)

        mainActivityViewModel.feedViewModel.widgets.add(Widget(appWidgetId, appWidgetInfo))
    }

    private fun removeWidget(widget: Widget) {
        mainActivityViewModel.feedViewModel.widgets.remove(widget)
    }

    fun showOrHideStatusBar(show: Boolean = true) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
         // Show / Hide the status bar.
        if(show == true) {
            window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
        } else {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }

         // Remember that you should never show the action bar if the
         // status bar is hidden, so hide that too if necessary.
         // actionBar?.hide()
    }

    @Composable
    fun Leancher() {
        LeancherTheme(
            content = {
                PagerLayout(mainActivityViewModel = mainActivityViewModel)
            }
        )
    }
}
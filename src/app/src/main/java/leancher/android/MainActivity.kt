package leancher.android

import android.Manifest
import android.app.NotificationManager
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
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
        appWidgetHost.startListening()

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

    fun getAppWidgetHost(): AppWidgetHost {
        return appWidgetHost
    }

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
        val viewState: MainActivityViewModel? = viewModelStateManager.restoreViewState()
        if (viewState != null) {
            mainActivityViewModel = viewState
            mainActivityViewModel.notificationCenterViewModel.notifications = mutableListOf()
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
        // TODO: fix bug -> should stop listening on widget changes while app is not in foreground
        // but call throws null pointer when attempting to read from field ->
        // 'com.android.server.appwidget.AppWidgetServiceImpl$ProviderId com.android.server.appwidget.AppWidgetServiceImpl$Provider.id'
        // appWidgetHost.stopListening()
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

    fun dismissNotification(notification: Notification) {
        val i = Intent(READ_COMMAND_ACTION)
        i.putExtra(COMMAND_KEY, DISMISS_NOTIFICATION)
        i.putExtra(RESULT_KEY, RESULT_OK)
        i.putExtra(RESULT_VALUE, notification.key)
        sendBroadcast(i)
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

    fun removeWidget(widget: Widget) {
        mainActivityViewModel.feedViewModel.widgets.removeIf {
                w -> w.id == widget.id
        }
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

    private fun testIntentStuff() {
        fun isIntentCallable(intent: Intent): Boolean =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY) // TODO: check whether this flag is needed
                .isNotEmpty()

        val testIntent = Intent("test")
        val sendIntent = Intent("send")
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:5551234"))
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.android.com"))

        Log.i("INTENTS", "test ?= ${isIntentCallable(testIntent)}")
        Log.i("INTENTS", "send ?= ${isIntentCallable(sendIntent)}")
        Log.i("INTENTS", "call ?= ${isIntentCallable(callIntent)}")
        Log.i("INTENTS", "web ?= ${isIntentCallable(webIntent)}")

        startActivity(Intent.createChooser(sendIntent, "Chose some app..."))
    }
}

/*

String action !including Namespace // https://developer.android.com/reference/android/content/Intent#Intent(java.lang.String)
Uri data | setDataAndNormalize // https://developer.android.com/reference/android/content/Intent#setData(android.net.Uri)
String type | setTypeAndNormalize // https://developer.android.com/reference/android/content/Intent#setTypeAndNormalize(java.lang.String)

Boolean showChooser // https://developer.android.com/training/basics/intents/sending#AppChooser
String? chooserTitle


 */
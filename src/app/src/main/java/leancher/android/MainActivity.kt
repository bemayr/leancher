package leancher.android

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientAnimationClock
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import leancher.android.domain.services.NotificationService
import leancher.android.ui.components.Pager
import leancher.android.ui.components.PagerState
import leancher.android.ui.pages.Feed
import leancher.android.ui.pages.Home
import leancher.android.ui.pages.NotificationCenter


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val res = checkSelfPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY)

        if (checkSelfPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Ask for permision
            requestPermissions(arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY), 1);
            // startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        else {
            // Permission has already been granted
        }

        // Hide the status bar.
        // window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        // actionBar?.hide()

        // setContentView(R.layout.activity_main)
        setContent {
            PagerTest()
        }
    }

    fun showNotifications() {
        if (isNotificationServiceEnabled()) {
            Log.i(TAG, "Notification enabled -- trying to fetch it")
            getNotifications()
        } else {
            Log.i(TAG, "Notification disabled -- Opening settings")
            startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }

    fun getNotifications() {
        Log.i(TAG, "Waiting for MyNotificationService")
        val myNotificationService: NotificationService? = getSystemService(NotificationService::class.java)
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
        val allNames = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if (allNames != null && !allNames.isEmpty()) {
            for (name in allNames.split(":").toTypedArray()) {
                if (packageName == ComponentName.unflattenFromString(name)!!.packageName) {
                    return true
                }
            }
        }
        return false
    }

    @Composable
    fun PagerTest() {
        val clock = AmbientAnimationClock.current
        val pagerState = remember(clock) { PagerState(clock, 1, 0, 2) }

        run {
            val clock = AmbientAnimationClock.current
            remember(clock) { PagerState(clock, 1, 0, 2) }
        }

        val modifier = Modifier.fillMaxSize()

        Pager(state = pagerState) {
            Column(
                    modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                when(page) {
                    0 -> Feed(page)
                    1 -> Home(page, launchIntent = { launchIntent() })
                    2 -> NotificationCenter(page)
                    else -> Home(page, launchIntent = { launchIntent() })
                }
            }
        }
    }

    private fun launchIntent() {
        val uriString = "https://stackoverflow.com/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uriString)
        startActivity(intent)
    }

    @Preview
    @Composable
    fun PreviewIntent() {
        PagerTest()
    }

}
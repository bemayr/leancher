package leancher.android

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import androidx.core.app.NotificationCompat
import leancher.android.ui.components.Pager
import leancher.android.ui.components.PagerState
import leancher.android.ui.components.TestButton
import leancher.android.ui.pages.Feed
import leancher.android.ui.pages.Home
import leancher.android.ui.pages.NotificationCenter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        actionBar?.hide()

        // setContentView(R.layout.activity_main)
        setContent {
            PagerTest()
        }

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
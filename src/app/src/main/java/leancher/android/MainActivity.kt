package leancher.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientAnimationClock
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import leancher.android.ui.components.Pager
import leancher.android.ui.components.PagerState
import leancher.android.ui.components.Paginator
import leancher.android.ui.pages.Feed
import leancher.android.ui.pages.Home
import leancher.android.ui.pages.NotificationCenter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContentView(R.layout.activity_main)
        setContent {
            PagerTest()
        }

    }

    @Composable
    fun PagerTest() {
        val clock = AmbientAnimationClock.current
        val pagerState = remember(clock) { PagerState(clock, 1, 0, 2) }
        val currentPage = pagerState.currentPage

        run {
            val clock = AmbientAnimationClock.current
            remember(clock) { PagerState(clock, 1, 0, 2) }
        }

        Pager(state = pagerState) {
            Row(Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)) {
                Column() {
                    when(page) {
                        0 -> Feed(page)
                        1 -> Home(page, launchIntent = { launchIntent() })
                        2 -> NotificationCenter(page)
                        else -> Home(page, launchIntent = { launchIntent() })
                    }
                }
            }
        }
        Row(Modifier
                .fillMaxSize(), verticalAlignment = Alignment.Bottom) {
            Column() {
                Paginator(pageAmount = 3, currentPage = currentPage)
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
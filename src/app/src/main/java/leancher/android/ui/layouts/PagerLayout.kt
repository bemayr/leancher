package leancher.android.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientAnimationClock
import androidx.compose.ui.unit.dp
import leancher.android.ui.components.Pager
import leancher.android.ui.components.PagerState
import leancher.android.ui.components.Paginator
import leancher.android.ui.pages.Feed
import leancher.android.ui.pages.Home
import leancher.android.ui.pages.NotificationCenter
import leancher.android.ui.states.FeedState

@Composable
fun PagerLayout(feedState: FeedState, launchIntentTest: () -> Unit) {
    val clock = AmbientAnimationClock.current
    val pagerState = remember(clock) { PagerState(clock, 1, 0, 2) }
    val currentPage = pagerState.currentPage

    run {
        val clock = AmbientAnimationClock.current
        remember(clock) { PagerState(clock, 1, 0, 2) }
    }

    Row(modifier = Modifier.background(MaterialTheme.colors.background)) {
        Pager(state = pagerState) {
            Row(Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)) {
                Column() {
                    when(page) {
                        0 -> Feed(page, feedState)
                        1 -> Home(page, launchIntent = { launchIntentTest() })
                        2 -> NotificationCenter(page)
                        else -> Home(page, launchIntent = { launchIntentTest() })
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
}
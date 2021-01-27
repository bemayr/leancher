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
import leancher.android.viewmodels.FeedViewModel
import leancher.android.viewmodels.HomeViewModel
import leancher.android.viewmodels.MainActivityViewModel
import leancher.android.viewmodels.NotificationCenterViewModel

@Composable
fun PagerLayout(vm: MainActivityViewModel) {
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
                        0 -> Feed(vm.feedViewModel)
                        1 -> Home(vm.homeViewModel)
                        2 -> NotificationCenter(vm.notificationCenterViewModel)
                        else -> TODO("this should no happen")
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
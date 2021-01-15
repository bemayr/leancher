package leancher.android.viewmodels

import androidx.lifecycle.ViewModel

class MainActivityViewModel(
    homeViewModel: HomeViewModel,
    feedViewModel: FeedViewModel,
    notificationCenterViewModel: NotificationCenterViewModel
) : ViewModel() {
    var homeViewModel = homeViewModel
    var feedViewModel = feedViewModel
    var notificationCenterViewModel = notificationCenterViewModel
}
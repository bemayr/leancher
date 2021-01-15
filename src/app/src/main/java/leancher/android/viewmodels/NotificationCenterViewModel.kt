package leancher.android.viewmodels

import androidx.lifecycle.ViewModel
import leancher.android.domain.models.Notification

class NotificationCenterViewModel(notifications: MutableList<Notification>?) : ViewModel() {
    var notifications = notifications
}
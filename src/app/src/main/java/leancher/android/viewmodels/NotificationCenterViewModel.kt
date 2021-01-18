package leancher.android.viewmodels

import androidx.lifecycle.ViewModel
import com.google.gson.annotations.Expose
import leancher.android.domain.models.Notification

class NotificationCenterViewModel(notifications: MutableList<Notification>?) : ViewModel() {
    @Expose(serialize = false, deserialize = false)
    var notifications = notifications
}
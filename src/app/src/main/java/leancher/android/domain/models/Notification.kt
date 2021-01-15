package leancher.android.domain.models

import android.graphics.drawable.Icon
import android.service.notification.StatusBarNotification

class Notification(
    key: String,
    title: String,
    text: String,
    iconId: Int,
    icon: Icon,
    originalNotificaion: StatusBarNotification
) {
    val key = key
    val title = title
    val text = text
    val iconId = iconId
    val icon = icon
    val originalNotificaion = originalNotificaion
}
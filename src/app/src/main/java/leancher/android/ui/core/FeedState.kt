package leancher.android.ui.core

import android.appwidget.AppWidgetProviderInfo

class Widget (id: Int, providerInfo: AppWidgetProviderInfo) {
    val id: Int = id
    val providerInfo: AppWidgetProviderInfo = providerInfo
}

class FeedState {
    val widgets: MutableList<Widget> = mutableListOf()
}
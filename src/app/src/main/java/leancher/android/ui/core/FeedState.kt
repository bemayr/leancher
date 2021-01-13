package leancher.android.ui.core

import android.appwidget.AppWidgetProviderInfo

class Widget (id: Int, providerInfo: AppWidgetProviderInfo) {
    val id: Int = id
    val providerInfo: AppWidgetProviderInfo = providerInfo
}

class FeedState(selectWidgetFun: () -> Unit) {
    val selectWidgetFun: () -> Unit = selectWidgetFun
    val widgets: MutableList<Widget> = mutableListOf()
}
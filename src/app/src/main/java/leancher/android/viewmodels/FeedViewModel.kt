package leancher.android.viewmodels

import android.appwidget.AppWidgetProviderInfo
import androidx.lifecycle.ViewModel

class Widget (id: Int, providerInfo: AppWidgetProviderInfo) {
    val id: Int = id
    val providerInfo: AppWidgetProviderInfo = providerInfo
}

class FeedViewModel(widgets: MutableList<Widget>): ViewModel() {
    val widgets = widgets
}
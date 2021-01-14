package leancher.android.ui.pages

import android.appwidget.AppWidgetHost
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ListPopupWindow.MATCH_PARENT
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.viewinterop.AndroidView
import leancher.android.R
import leancher.android.domain.models.PageTitle
import leancher.android.ui.components.ActionButton
import leancher.android.ui.components.TitleCard
import leancher.android.ui.states.FeedState

lateinit var feedTitleModel: PageTitle

@Composable
fun Feed(page: Int, feedState: FeedState) {
    val context = ContextAmbient.current

    feedTitleModel = PageTitle(context.getString(R.string.page_widget_feed), "Your widget feed", R.drawable.cool)

    Row { TitleCard(pageTitle = feedTitleModel) { ActionButton(text = "Add Widget", action = feedState.selectWidgetFun) } }
//    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.Center) { ActionButton(text = "Add Widget", action = feedState.selectWidgetFun) }
    Row { WidgetHostView(feedState = feedState) }
}

@Composable
fun WidgetHostView(feedState: FeedState) {
    val context = AmbientContext.current
    val appWidgetHost = AppWidgetHost(context, 1024)

    ScrollableColumn() {
        feedState.widgets.forEach { w ->
            AndroidView(viewBlock = { ctx ->
                appWidgetHost.createView(context, w.id, w.providerInfo).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                }
            })
        }
    }
}
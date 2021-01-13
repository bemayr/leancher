package leancher.android.ui.pages

import android.appwidget.AppWidgetHost
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ListPopupWindow.MATCH_PARENT
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import leancher.android.R
import leancher.android.domain.models.PageTitle
import leancher.android.ui.components.TitleCard
import leancher.android.ui.core.FeedState

var feedTitleModel = PageTitle("Feed", "Your widget feed", R.drawable.cool)

@Composable
fun Feed(page: Int, feedState: FeedState) {
    val state = remember { mutableStateOf(0) }

    TitleCard(pageTitle = feedTitleModel)
    WidgetHostView(feedState = feedState, state = state)
}

@Composable
fun WidgetHostView(feedState: FeedState, state: MutableState<Int>) {
    val context = AmbientContext.current
    val appWidgetHost = AppWidgetHost(context, 1024)

    Column() {

        AndroidView(viewBlock = { ctx ->
            //Here you can construct your View
            android.widget.Button(ctx).apply {
                text = "Some Button"
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                setOnClickListener {
                    state.value++
                }
            }
        }, modifier = Modifier.padding(8.dp))

        AndroidView(viewBlock = { ctx ->
            //Here you can construct your View
            TextView(ctx).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                text = "abc"
                setTextColor(Color.WHITE)
            }
        }, update = {
            it.text = "You have clicked the buttons: " + state.value.toString() + " times"
        })

        feedState.widgets.forEach { w ->
            AndroidView(viewBlock = { ctx ->
                appWidgetHost.createView(context, feedState.widgets.first().id, feedState.widgets.first().providerInfo).apply {
                    layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                }
            })
        }
    }
}
package leancher.android.ui.pages

import android.appwidget.AppWidgetHostView
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ListPopupWindow.MATCH_PARENT
import android.widget.TextView
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import leancher.android.ui.core.FeedState
import leancher.android.ui.theme.White

@Composable
fun Feed(page: Int, feedState: FeedState) {
    val state = remember { mutableStateOf(0) }

    Text(text = "Feed, Page: $page", color = White())
    WidgetHostView(feedState = feedState, state = state)
}

@Composable
fun WidgetHostView(feedState: FeedState, state: MutableState<Int>) {
    if (feedState.widgets.size > 0) {
        AndroidView(viewBlock = { ctx ->
            //Here you can construct your View
            android.widget.Button(ctx).apply {
                text = "MyAndroidButton"
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
            }
        }, update = {
            it.text = "You have clicked the buttons: " + state.value.toString() + " times"
        })

        AndroidView(viewBlock = { ctx ->
            AppWidgetHostView(ctx).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                setAppWidget(feedState.widgets[0].id, feedState.widgets[0].providerInfo)
            }
        })
    }
}
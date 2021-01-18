package leancher.android.ui.pages

import android.app.Activity
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.ListPopupWindow.MATCH_PARENT
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.longPressGestureFilter
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.startActivityForResult
import leancher.android.MainActivity
import leancher.android.R
import leancher.android.domain.models.PageTitle
import leancher.android.ui.components.ActionButton
import leancher.android.ui.components.ActionDialog
import leancher.android.ui.components.IconButton
import leancher.android.ui.components.TitleCard
import leancher.android.viewmodels.FeedViewModel

@Composable
fun Feed(feedViewModel: FeedViewModel) {
    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }
    val context = ContextAmbient.current
    val feedTitleModel = PageTitle(
        context.getString(R.string.page_widget_feed),
        "Your widget feed",
        R.drawable.cool
    )

    Row {
        TitleCard(pageTitle = feedTitleModel) {
            ActionButton(
                text = "Add Widget",
                action = {
                    val activity: MainActivity = context as MainActivity
                    activity.selectWidget()
                    // val appWidgetHost = AppWidgetHost(context, 1024)
                    // val appWidgetId = appWidgetHost.allocateAppWidgetId()
                    // val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
                    // pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    // val activity: Activity = context as Activity
                    // startActivityForResult(activity, pickIntent, 9, null)
                })
        }
    }

    Row { WidgetHostView(feedViewModel = feedViewModel) }

    ActionDialog(
        "Remove Widget?", "Are you sure? Widget will be removed ...",
        showDialog, setShowDialog,
        { println("Confirm") }, "Yes",
        {  }, "No")
}

@Composable
fun WidgetHostView(feedViewModel: FeedViewModel) {
    val context = AmbientContext.current
    val activity: MainActivity = context as MainActivity
    val appWidgetHost = AppWidgetHost(context, 1024)

    // https://developer.android.google.cn/reference/kotlin/androidx/compose/foundation/package-summary

    ScrollableColumn() {
        feedViewModel.widgets.forEach { w ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.width(350.dp)) {
                    AndroidView(viewBlock = { ctx ->
                        appWidgetHost.createView(context, w.id, w.providerInfo).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                        }
                    })
                }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                    IconButton(icon = Icons.Filled.Delete, action = { activity.removeWidget(w) })
                }
            }
        }
    }
}
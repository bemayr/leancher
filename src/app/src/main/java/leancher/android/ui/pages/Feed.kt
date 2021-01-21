package leancher.android.ui.pages

import android.appwidget.AppWidgetHost
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import leancher.android.MainActivity
import leancher.android.R
import leancher.android.domain.models.PageTitle
import leancher.android.ui.components.ActionButton
import leancher.android.ui.components.ActionDialog
import leancher.android.ui.components.IconButton
import leancher.android.ui.components.TitleCard
import leancher.android.ui.util.TranslateString
import leancher.android.viewmodels.FeedViewModel


@Composable
fun Feed(feedViewModel: FeedViewModel) {
    val context = AmbientContext.current

    val feedTitleModel = PageTitle(
        context.getString(R.string.page_widget_feed),
        context.getString(R.string.hows_your_day),
        R.drawable.home
    )

    Row {
        Column(Modifier.padding(10.dp)) {
            TitleCard(pageTitle = feedTitleModel) {
                IconButton(icon = Icons.Filled.Add, action = {
                    val activity: MainActivity = context as MainActivity
                    activity.selectWidget()
                }, context.getString(R.string.add))
            }
        }
    }

    Row { WidgetHostView(feedViewModel = feedViewModel) }
}

@Composable
fun WidgetHostView(feedViewModel: FeedViewModel) {
    val context = AmbientContext.current
    val activity: MainActivity = context as MainActivity
    val appWidgetHost = activity.getAppWidgetHost()

    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    // https://developer.android.google.cn/reference/kotlin/androidx/compose/foundation/package-summary

    // TODO: get dialog out of forEach loop (find suitable workaround for callback (with parameter)
    // TODO: bugfix every time when a widget is deleted, the last one in the list is not rendered any more - why?

    ScrollableColumn() {
        feedViewModel.widgets.forEach { w ->
            ActionDialog(
                title = context.getString(R.string.remove_widget_title), text = context.getString(R.string.remove_widget_text),
                showDialog = showDialog, setShowDialog = setShowDialog,
                confirmAction = { activity.removeWidget(w) },
                dismissAction = { },
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = { },
                        onLongClick = {
                            setShowDialog(true)
                        }), horizontalAlignment = Alignment.CenterHorizontally) {
                    AndroidView(
                        viewBlock = { ctx ->
                            appWidgetHost.createView(context, w.id, w.providerInfo).apply {
                                // layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                                setOnLongClickListener {
                                    setShowDialog(true)
                                    true // <- set to true
                                }
                            }
                        })
                }
            }
        }
    }
}
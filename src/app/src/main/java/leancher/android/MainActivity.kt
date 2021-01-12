package leancher.android

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientAnimationClock
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import leancher.android.ui.components.Pager
import leancher.android.ui.components.PagerState
import leancher.android.ui.core.FeedState
import leancher.android.ui.core.Widget
import leancher.android.ui.pages.Feed
import leancher.android.ui.pages.Home
import leancher.android.ui.pages.NotificationCenter
import java.util.*


class MainActivity : AppCompatActivity() {

    private val APPWIDGET_HOST_ID = 1024
    private val REQUEST_CREATE_APPWIDGET = 5
    private val REQUEST_PICK_APPWIDGET = 9

    private var feedState: FeedState = FeedState()

    private lateinit var mAppWidgetManager: AppWidgetManager
    private lateinit var mAppWidgetHost: AppWidgetHost

    private lateinit var mainlayout: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//         setContentView(R.layout.activity_main)
        setContent {
            PagerTest()
        }

        mAppWidgetManager = AppWidgetManager.getInstance(this)
        mAppWidgetHost = AppWidgetHost(this, APPWIDGET_HOST_ID)

//        mainlayout = findViewById(R.id.main_layout);

        selectWidget()
    }

    override fun onStart() {
        super.onStart()
        mAppWidgetHost.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAppWidgetHost.stopListening()
    }

    @Composable
    fun PagerTest() {
        val clock = AmbientAnimationClock.current
        val pagerState = remember(clock) { PagerState(clock) }

        run {
            val clock = AmbientAnimationClock.current
            remember(clock) { PagerState(clock) }
        }

        pagerState.maxPage = (2).coerceAtLeast(0)

        val modifier = Modifier.fillMaxSize()

        Pager(state = pagerState) {
            Column(
                    modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                when(page) {
                    0 -> Feed(page, feedState = feedState)
                    1 -> Home(page, launchIntent = { launchIntent() })
                    2 -> NotificationCenter(page)
                    else -> Home(page, launchIntent = { launchIntent() })
                }
            }
        }
    }

    @Preview
    @Composable
    fun PreviewIntent() {
        PagerTest()
    }

    private fun launchIntent() {
        val uriString = "https://stackoverflow.com/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uriString)
        startActivity(intent)
    }

    fun selectWidget() {
        val appWidgetId = mAppWidgetHost.allocateAppWidgetId()
        val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
//        addEmptyData(pickIntent)
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET)
    }

    fun addEmptyData(pickIntent: Intent) {
        val customInfo: ArrayList<out Parcelable> = ArrayList<Parcelable>()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo)
        val customExtras: ArrayList<out Parcelable> = ArrayList<Parcelable>()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_APPWIDGET) {
                configureWidget(data)
            } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                if (data != null) {
                    createWidget(data)
                }
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {
            val appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
            if (appWidgetId != -1) {
                mAppWidgetHost.deleteAppWidgetId(appWidgetId)
            }
        }
    }

    private fun configureWidget(data: Intent?) {
        val extras = data!!.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId)
        if (appWidgetInfo.configure != null) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
            intent.component = appWidgetInfo.configure
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET)
        } else {
            createWidget(data)
        }
    }

    fun createWidget(data: Intent) {
        val extras = data.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId)
        val hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo)
        hostView.setAppWidget(appWidgetId, appWidgetInfo)
        feedState.widgets.add(Widget(appWidgetId, appWidgetInfo))
//        mainlayout.addView(hostView)
    }

    fun removeWidget(hostView: AppWidgetHostView) {
        mAppWidgetHost.deleteAppWidgetId(hostView.appWidgetId)
        mainlayout.removeView(hostView)
    }

}
package leancher.android.ui.components

import android.R
import android.app.Activity.RESULT_CANCELED
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ContextAmbient
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import java.util.*


class AppWidgetHostState {
//    val context = ContextAmbient.current
//
//    val mAppWidgetManager = AppWidgetManager.getInstance(context)
//    val mAppWidgetHost = android.appwidget.AppWidgetHost(context, R.id.APPWIDGET_HOST_ID)
//
//    fun selectWidget() {
//        val appWidgetId: Int = this.mAppWidgetHost.allocateAppWidgetId()
//        val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
//        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
//        addEmptyData(pickIntent)
//        ActivityCompat.startActivityForResult(pickIntent, R.id.REQUEST_PICK_APPWIDGET)
//    }
//
//    fun addEmptyData(pickIntent: Intent) {
//        val customInfo = ArrayList<AppWidgetProviderInfo>()
//        pickIntent.putParcelableArrayListExtra(
//                AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo)
//        val customExtras = ArrayList<Bundle>()
//        pickIntent.putParcelableArrayListExtra(
//                AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras)
//    }
//
//    protected fun onActivityResult(requestCode: Int, resultCode: Int,
//                                   data: Intent?) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_PICK_APPWIDGET) {
//                configureWidget(data)
//            } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
//                createWidget(data)
//            }
//        } else if (resultCode == RESULT_CANCELED && data != null) {
//            val appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
//            if (appWidgetId != -1) {
//                mAppWidgetHost.deleteAppWidgetId(appWidgetId)
//            }
//        }
//    }
//
//    private fun configureWidget(data: Intent?) {
//        val extras = data!!.extras
//        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
//        val appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId)
//        if (appWidgetInfo.configure != null) {
//            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
//            intent.component = appWidgetInfo.configure
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
//            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET)
//        } else {
//            createWidget(data)
//        }
//    }
}

@Composable
fun AppWidgetHost() {

}
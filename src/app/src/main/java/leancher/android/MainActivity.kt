package leancher.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)

        setContent {
            IntentButton("Stackoverflow")
        }

        /* val pm = getPackageManager()
        val main = Intent(Intent.ACTION_MAIN, null)
        main.addCategory(Intent.CATEGORY_LAUNCHER)
        val launchables = pm.queryIntentActivities(main, 0)

        Collections.sort(launchables, ResolveInfo.DisplayNameComparator(pm));
        launchables.forEach { l -> println(l.serviceInfo.name) } */

        /* val btn = findViewById(R.id.launchIntent) as Button

        btn.setOnClickListener {
            launchIntent()

        }*/
    }

    private fun launchIntent() {
        val uriString = "https://stackoverflow.com/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uriString)

        if (intent != null) {
            startActivity(intent);
        } else {
            toast("Intent null.")
        }
    }

    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun IntentButton(name: String) {
        Button(onClick = {
            launchIntent()
        }) {
            Text(text = "Launch $name!")
        }
    }

    @Preview
    @Composable
    fun PreviewIntent() {
        IntentButton("Stackoverflow")
    }

}
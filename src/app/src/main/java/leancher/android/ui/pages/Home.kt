package leancher.android.ui.pages

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.AlarmClock
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.unit.dp
import leancher.android.R
import leancher.android.domain.models.PageTitle
import leancher.android.ui.components.TitleCard
import leancher.android.viewmodels.HomeViewModel

@Composable
fun Home(homeViewModel: HomeViewModel) {
    val context = AmbientContext.current

    val homeTitleModel = PageTitle(context.getString(R.string.page_home), "A human centered launcher", R.drawable.leancher)

    Row {
        Column(Modifier.padding(10.dp)) {
            TitleCard(pageTitle = homeTitleModel, null)
        }
    }

    Row {
        Column(Modifier.padding(10.dp)) {
            Text("TODO:", style = MaterialTheme.typography.body1)
            Text("I wanna ... ", style = MaterialTheme.typography.body1)
            Text("... start an <app>", style = MaterialTheme.typography.body1)
            Button(onClick = { testIntentStuff(context) }){ Text("Test Intent Stuff") }
        }
    }
    //    Text(text = "Leancher Home, Page: $page", color = White())
    //    IntentButton(name = "Test Intent", launchIntent = launchIntent)
}

@Composable
fun IntentButton(name: String, launchIntent: () -> Unit) {
    Button(onClick = {
        launchIntent()
    }) {
        Text(text = "Launch $name!")
    }
}

private fun testIntentStuff(context: Context) {
    context.apply {
        fun isIntentCallable(intent: Intent): Boolean =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY) // TODO: check whether this flag is needed
                .isNotEmpty()

        val testIntent = Intent("test")
        val sendIntent = Intent("send")
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:5551234"))
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.android.com"))

        Log.i("INTENTS", "test ?= ${isIntentCallable(testIntent)}")
        Log.i("INTENTS", "send ?= ${isIntentCallable(sendIntent)}")
        Log.i("INTENTS", "call ?= ${isIntentCallable(callIntent)}")
        Log.i("INTENTS", "web ?= ${isIntentCallable(webIntent)}")

        fun createAlarm(message: String, hour: Int, minutes: Int) {
            val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, message)
                putExtra(AlarmClock.EXTRA_HOUR, hour)
                putExtra(AlarmClock.EXTRA_MINUTES, minutes)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

//        startActivity(Intent(AlarmClock.ACTION_SET_TIMER).apply {
//            putExtra(AlarmClock.EXTRA_LENGTH, 600)
//            putExtra(AlarmClock.EXTRA_MESSAGE, "💤")
//            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
//        })
        startActivity(Intent(AlarmClock.ACTION_SET_TIMER))


        // start Activity
        //startActivity(callIntent)

        // start Activity with Chooser
        //startActivity(Intent.createChooser(sendIntent, "Chose some app..."))

        // start Activity for Result: https://developer.android.com/training/basics/intents/result
    }
}

// "set a timer",
// Intent(android.intent.action.SET_TIMER)

// "nap",
// Intent(android.intent.action.SET_TIMER)
// {
//    android.intent.extra.alarm.LENGTH: 720
//    android.intent.extra.alarm.MESSAGE: "💤"
//    android.intent.extra.alarm.SKIP_UI: true
// },
// Toast("wet dreams")

val timer = LeancherIntent(
    listOf(
        LeancherIntent.Block.Text("set a timer"),
        LeancherIntent.Block.Action.Setter(
            LeancherIntent.Block.Action.IntentDefinition(
                "android.intent.action.SET_TIMER"
        ))
    )
)

val nap = LeancherIntent(
    listOf(
        LeancherIntent.Block.Text("nap"),
        LeancherIntent.Block.Action.Setter(
            LeancherIntent.Block.Action.IntentDefinition(
                "android.intent.action.SET_TIMER",
                extras = listOf(
                    LeancherIntent.Block.Action.IntentDefinition.Extra(
                        "android.intent.extra.alarm.LENGTH",
                        LeancherIntent.Block.Action.IntentDefinition.Value.Constant(720)),
                    LeancherIntent.Block.Action.IntentDefinition.Extra(
                        "android.intent.extra.alarm.MESSAGE",
                        LeancherIntent.Block.Action.IntentDefinition.Value.Constant("💤")),
                    LeancherIntent.Block.Action.IntentDefinition.Extra(
                        "android.intent.extra.alarm.SKIP_UI",
                        LeancherIntent.Block.Action.IntentDefinition.Value.Constant(true))
                ))),
        LeancherIntent.Block.Message("wet dreams")))


class LeancherIntent(val blocks: List<Block>) {
    sealed class Block {
        data class Text(val content: String) : Block()

        sealed class Action : Block() {
            class Getter(val definition: IntentDefinition, val reference: IntentDefinition.Value.Reference, val renderer: Renderer) : Action()
            class Setter(val definition: IntentDefinition) : Action()

            data class Renderer(val id: String)

            data class IntentDefinition(val id: String, val uri: Value? = null, val data: Value? = null, val extras: List<Extra>? = null) {
                data class Extra(val id: String, val value: Value)

                sealed class Value {
                    data class Reference(val key: String) : Value()
                    data class Constant(val value: Any) : Value()
                }
            }
        }

        data class Message(val content: String) : Block()
    }
}

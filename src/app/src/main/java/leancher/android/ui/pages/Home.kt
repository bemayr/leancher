package leancher.android.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import leancher.android.R
import leancher.android.domain.models.PageTitle
import leancher.android.ui.components.TitleCard
import leancher.android.ui.theme.StandardText

var titleModel = PageTitle("Home", "A human centered launcher experience", R.drawable.cool)

@Composable
fun Home(page: Int, launchIntent: () -> Unit) {
    Row {
        Column(Modifier.padding(20.dp)) {
            TitleCard(pageTitle = titleModel, null)
            Text("TODO:", style = StandardText())
            Text("I wanna ... ", style = StandardText())
            Text("... start an <app>", style = StandardText())
        }
    }
    //    Text(text = "Lenacher Home, Page: $page", color = White())
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
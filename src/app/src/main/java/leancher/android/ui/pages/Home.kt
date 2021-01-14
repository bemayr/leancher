package leancher.android.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import leancher.android.R
import leancher.android.domain.models.PageTitle
import leancher.android.ui.components.TitleCard

lateinit var homeTitleModel: PageTitle

@Composable
fun Home(page: Int, launchIntent: () -> Unit) {
    val context = ContextAmbient.current

    homeTitleModel = PageTitle(context.getString(R.string.page_home), "A human centered launcher experience", R.drawable.cool)

    Row {
        Column(Modifier.padding(20.dp)) {
            TitleCard(pageTitle = homeTitleModel, null)
            Text("TODO:", style = MaterialTheme.typography.body1)
            Text("I wanna ... ", style = MaterialTheme.typography.body1)
            Text("... start an <app>", style = MaterialTheme.typography.body1)
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
package leancher.android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import leancher.android.domain.models.PageTitle
import leancher.android.ui.theme.Gray
import leancher.android.ui.theme.Heading
import leancher.android.ui.theme.StandardText

@Composable
fun TitleCard(pageTitle: PageTitle, actionButton: @Composable() (() -> Unit)?) {
    Card(elevation = 4.dp, backgroundColor = Gray(), modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = CenterVertically,
            modifier = Modifier.padding(16.dp)) {

            Image(imageResource(id = pageTitle.imageResource),
                    modifier = Modifier.preferredSize(50.dp, 50.dp),
                    contentScale = ContentScale.Crop)

            Column(modifier = Modifier.padding(10.dp)) {
                Text(pageTitle.title, style = Heading())
                Text(pageTitle.longText, style = StandardText())
            }

            if(actionButton != null) {
                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.End) {
                    actionButton()
                }
            }
        }
    }
}
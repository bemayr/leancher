package leancher.android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import leancher.android.domain.models.PageTitle
import leancher.android.ui.theme.Gray
import leancher.android.ui.theme.Heading
import leancher.android.ui.theme.StandardText
import leancher.android.ui.theme.White

@Composable
fun TitleCard(pageTitle: PageTitle) {
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
        }
    }
}
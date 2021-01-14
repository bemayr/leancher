package leancher.android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp

@Composable
fun IconButton(icon: Int, action: () -> Unit) {
    FloatingActionButton(onClick = {
        action()
    }) {
        Image(imageResource(id = icon),
                modifier = Modifier.preferredSize(25.dp, 25.dp),
                contentScale = ContentScale.Crop)
    }
}
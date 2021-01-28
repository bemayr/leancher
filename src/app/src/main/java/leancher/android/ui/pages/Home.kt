package leancher.android.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import leancher.android.R
import leancher.android.domain.models.PageTitle
import leancher.android.ui.components.TitleCard
import leancher.android.domain.intents.LeancherIntent
import leancher.android.viewmodels.HomeViewModel
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.platform.AmbientContext

@Composable
fun Home(vm: HomeViewModel) {
    val context = AmbientContext.current

    val homeTitleModel = PageTitle(
        context.getString(R.string.page_home),
        context.getString(R.string.launcher_experience),
        R.drawable.leancher
    )

    // TODO
    var textValue by savedInstanceState { "1234567812345678" }

    Row {
        Column(Modifier.padding(10.dp)) {
            TitleCard(pageTitle = homeTitleModel, null)
        }
    }

    Column(Modifier.padding(10.dp)) {
        Text(text = vm.greeting)
        Text(text = "Step Index: ${vm.stepIndex}")
        IWanna()
        Blocks(vm.blocks, vm.renderers)
        NextBlock(vm.nextBlockOptions, vm::blockSelected, vm.renderers)
    }
}


@Composable
fun IWanna() = Text("i wanna …", style = MaterialTheme.typography.body1)

@Composable
fun Blocks(blocks: List<LeancherIntent.Block>, renderers: HomeViewModel.Renderers) =
    Column { blocks.forEach { Block(it, renderers) } }

@Composable
fun NextBlock(
    nextBlockOptions: List<LeancherIntent.Block>,
    blockSelected: (LeancherIntent.Block) -> Unit,
    renderers: HomeViewModel.Renderers
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    TextField(
        value = textState.value,
        onValueChange = { textState.value = it }
    )
    Text("Searching for: " + textState.value.text)
    nextBlockOptions.forEach { block ->
        Card(Modifier.clickable(onClick = { blockSelected(block) })) { Block(block, renderers) }
    }
}

@Composable
fun Block(block: LeancherIntent.Block, renderers: HomeViewModel.Renderers) = when(block) {
    is LeancherIntent.Block.Text -> Text(text = block.content)
    is LeancherIntent.Block.Action.Getter.InputGetter -> (renderers.input[block.reference.key] ?: { Text("no renderer for ${block.reference.key} specified") }).invoke()
    is LeancherIntent.Block.Action.Getter.IntentGetter -> TODO("render result")
    is LeancherIntent.Block.Action.Setter.ReferenceSetter -> { }
    is LeancherIntent.Block.Action.Setter.IntentDefinitionSetter -> { }
    is LeancherIntent.Block.Message -> Text(text = block.content)
}
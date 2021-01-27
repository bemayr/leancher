package leancher.android.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import leancher.android.domain.intents.LeancherIntent
import leancher.android.domain.intents.*
import leancher.android.viewmodels.HomeViewModel
import androidx.compose.runtime.livedata.observeAsState

val intents = listOf<LeancherIntent>(alarm, timer, nap)

@Composable
fun Home(vm: HomeViewModel) =
    Column(Modifier.padding(8.dp)) {
        Text(text = vm.greeting)
        Text(text = "Step Index: ${vm.stepIndex}")
        IWanna()
        Blocks(vm.blocks)
        NextBlock(vm.nextBlockOptions, vm::blockSelected)
    }


@Composable
fun IWanna() = Text("i wanna …", style = MaterialTheme.typography.body1)

@Composable
fun Blocks(blocks: List<LeancherIntent.Block>) =
    Column { blocks.forEach { Block(it) } }

@Composable
fun NextBlock(
    nextBlockOptions: List<LeancherIntent.Block>,
    blockSelected: (LeancherIntent.Block) -> Unit
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    TextField(
        value = textState.value,
        onValueChange = { textState.value = it }
    )
    Text("Searching for: " + textState.value.text)
    nextBlockOptions.forEach { block ->
        Card(Modifier.clickable(onClick = { blockSelected(block) })) { Block(block) }
    }
}

@Composable
fun Block(block: LeancherIntent.Block) {
    when(block) {
        is LeancherIntent.Block.Text -> Text(text = block.content)
        is LeancherIntent.Block.Action.Getter.InputGetter -> TODO("render input")
        is LeancherIntent.Block.Action.Getter.IntentGetter -> TODO("render result")
        is LeancherIntent.Block.Action.Setter.ReferenceSetter -> { }
        is LeancherIntent.Block.Action.Setter.IntentDefinitionSetter -> { }
        is LeancherIntent.Block.Message -> Text(text = block.content)
    }
}
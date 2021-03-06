package leancher.android.viewmodels

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import leancher.android.domain.intents.BlockId
import leancher.android.domain.intents.LeancherIntent
import leancher.android.domain.intents.LeancherIntent.Block.Action.IntentDefinition.Additional.Data
import leancher.android.domain.intents.LeancherIntent.Block.Action.IntentDefinition.Additional.Type
import leancher.android.domain.intents.LeancherIntent.Block.Action.IntentDefinition.Value
import leancher.android.domain.intents.LeancherIntent.Block.Action.IntentDefinition.Value.Constant
import leancher.android.domain.intents.LeancherIntent.Block.Action.IntentDefinition.Value.Reference
import java.io.Serializable

fun List<LeancherIntent>.blocksFor(step: Int): List<LeancherIntent.Block> =
    mapNotNull { intent -> intent.blocks.getOrNull(step, ) }
fun List<LeancherIntent.Block>.ids(): List<BlockId> =
    map(LeancherIntent.Block::id)

class HomeModel(
    val store: IScopedStateStore
) {}

interface IScopedStateStore {
    fun <TState>saveState (key: String, state: TState): Unit
    fun <TState>loadState (key: String): TState?
}

class ViewModelFactory<TViewModel : ViewModel?>(
    private val viewModelClass: Class<TViewModel>,
    private val getViewModel: () -> TViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(viewModelClass) -> getViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

class HomeViewModel(private val model: HomeModel, private val actions: Actions) : ViewModel() {

    private val intents = leancher.android.domain.intents.intents

    var stepIndex by mutableStateOf(0)
        private set

    var blocks: List<LeancherIntent.Block> by mutableStateOf(listOf())
        private set

    var nextBlockOptions: List<LeancherIntent.Block> by mutableStateOf(nextBlockOptions())
        private set

    var greeting: String by mutableStateOf("initial")
        private set

    fun blockSelected(block: LeancherIntent.Block) {
        when(block) {
            is LeancherIntent.Block.Action.Getter.InputGetter -> Log.i("HOMEVIEWMODEL", "Input Getter")
            is LeancherIntent.Block.Action.Getter.IntentGetter -> Log.i("HOMEVIEWMODEL", "Intent Getter")
            is LeancherIntent.Block.Action.Setter.ReferenceSetter -> actions.executeIntent(values[block.reference.key] as Intent)
            is LeancherIntent.Block.Action.Setter.IntentDefinitionSetter -> actions.executeIntent(createIntent(block.definition))
            else -> { /* no side effect has to be executed */ }
        }

        stepIndex++
        blocks += block
        nextBlockOptions = nextBlockOptions()

        model.store.saveState("greeting", "Hallo")

        greeting = model.store.loadState("greeting") ?: "not found"

        when (nextBlockOptions.size) {
            0 -> {
                stepIndex = 0
                blocks = listOf()
                nextBlockOptions = nextBlockOptions()
            }
            1 -> blockSelected(nextBlockOptions[0])
        }
    }

    private fun nextBlockOptions() = intents.filter { intent -> intent.matches(blocks.ids()) }.blocksFor(stepIndex)

    private val values: Map<String, Any> = mapOf()

    private fun <T>resolveValue(registry: Map<String, Any>, value: Value): T? =
        when(value) {
            is Reference -> registry[value.key]
            is Constant -> value.value
        } as T?

    private fun createIntent(definition: LeancherIntent.Block.Action.IntentDefinition): Intent = Intent(definition.id).apply {
        when(definition.additional) {
            is Data -> setDataAndNormalize(Uri.parse(resolveValue(values, definition.additional.content)))
            is Type -> setTypeAndNormalize(resolveValue(values, definition.additional.content))
        }

        definition.extras?.forEach { definition -> putExtra(definition.id, resolveValue<Serializable>(values, definition.value)) }
    }

    data class Actions(
        val executeIntent: (Intent) -> Unit,
        val isIntentCallable: (Intent) -> Boolean
    )
}
package eu.noharmdan.showcase.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import eu.noharmdan.showcase.base.BaseViewModel
import eu.noharmdan.showcase.base.ViewCommand
import eu.noharmdan.showcase.base.ViewEvent
import eu.noharmdan.showcase.base.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

val baseCoroutineExceptionHandler = CoroutineExceptionHandler { _, ex ->
    throw ex
    // todo decide of exception should be thrown or only reported to e.g. Firebase Crashlytics etc.
}

/**
 * Convenience extension function for lifecycle-aware collection of view model state.
 */
@Composable
fun <STATE : ViewState> BaseViewModel<STATE, *, *>.collectState(): STATE {
    return stateFlow.collectAsStateWithLifecycle(initialValue = initialState).value
}

/**
 * Convenience extension function for obtaining a remembered function type that can be passed further down the composition tree and be used to send events to the viewModel.
 */
@Composable
fun <T : ViewEvent> BaseViewModel<*, T, *>.getOnEvent(): ((T) -> Unit) {
    // Posting should be considered an UI action, thus done on the compose scope to ensure cancellations are propagated correctly.
    val scope = rememberCoroutineScope() + baseCoroutineExceptionHandler

    return remember {
        { event ->
            scope.launch {
                eventChannel.send(event)
            }
        }
    }
}

/**
 * Convenience function for collecting the one-time commands from a view model. It ensures they are delivered only when the UI lifecycle is in the correct state.
 */
@Composable
fun <COMMAND : ViewCommand> CollectCommand(
    viewModel: BaseViewModel<*, *, COMMAND>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    block: suspend (COMMAND) -> Unit
) {
    return LaunchedEffect(key1 = Unit) {
        viewModel.commandFlow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach(block)
            .launchIn(this + baseCoroutineExceptionHandler)
    }
}
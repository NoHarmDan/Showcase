package eu.noharmdan.showcase.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import eu.noharmdan.showcase.util.baseCoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

/**
 * Base MVI view model class to be extended by all view models.
 *
 * Reflects the current view state through [stateFlow] and [currentState], receives events through [eventChannel] and publishes one-time commands through [commandFlow].
 *
 * Establishes coroutine scopes for all types of operations - [uiScope], [ioScope] and [defaultScope].
 */
abstract class BaseViewModel<STATE : ViewState, EVENT : ViewEvent, COMMAND : ViewCommand>(application: Application, val initialState: STATE) : AndroidViewModel(application), KoinComponent {

    private val uiJob = SupervisorJob()
    private val ioJob = SupervisorJob()
    private val defaultJob = SupervisorJob()

    /**
     * Use this dispatcher to run a coroutine on the main Android thread.
     * This should be used only for interacting with the UI and performing quick work.
     */
    protected val uiScope = CoroutineScope(Dispatchers.Main + uiJob + baseCoroutineExceptionHandler)

    /**
     * This dispatcher is optimized to perform disk or network I/O outside of the main thread.
     */
    protected val ioScope = CoroutineScope(Dispatchers.IO + ioJob + baseCoroutineExceptionHandler)

    /**
     * This dispatcher is optimized to perform CPU-intensive work outside of the main thread.
     */
    protected val defaultScope = CoroutineScope(Dispatchers.Default + defaultJob + baseCoroutineExceptionHandler)

    private val _stateFlow = MutableStateFlow(initialState)

    /**
     * Contains the current instance of [STATE].
     */
    val stateFlow: Flow<STATE> = _stateFlow.asStateFlow()

    /**
     * The channel to send events into from the UI.
     *
     * @see getOnEvent
     */
    val eventChannel = Channel<EVENT>(Channel.UNLIMITED)

    private val _commandFlow = MutableSharedFlow<COMMAND>()

    /**
     * The flow used to send one-time commands from viewModel to the UI.
     */
    val commandFlow: Flow<COMMAND> = _commandFlow.asSharedFlow()

    init {
        handleEvents()
    }

    /**
     * Updates view state with a new value.
     */
    protected fun updateState(body: STATE.() -> STATE) {
        synchronized(_stateFlow) {
            _stateFlow.value = body(_stateFlow.value)
        }
    }

    /**
     * Gets the current view state.
     */
    protected fun currentState(): STATE {
        synchronized(_stateFlow) {
            return _stateFlow.value
        }
    }

    /**
     * Implement to handle a ViewModel specific event.
     */
    abstract fun onEvent(event: EVENT)

    /**
     * Ensures that all incoming UI events are consumed and processed through [onEvent].
     */
    private fun handleEvents() {
        defaultScope.launch {
            eventChannel.consumeAsFlow().collect {
                onEvent(it)
            }
        }
    }

    /**
     * Call to send a one-time command to the UI.
     */
    protected suspend fun sendCommand(command : COMMAND) {
        _commandFlow.emit(command)
    }

    /**
     * This function simply ensures that emitting into a flow always happens on the [defaultScope].
     */
    protected fun <T> FlowCollector<T>.emitAsync(value: T) {
        defaultScope.launch {
            emit(value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        uiJob.cancel()
        ioJob.cancel()
        defaultJob.cancel()
        ioScope.cancel()
        uiScope.cancel()
        defaultScope.cancel()
    }

}
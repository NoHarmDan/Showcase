package eu.noharmdan.showcase.scene.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import eu.noharmdan.common.ui.ConfirmationDialog
import eu.noharmdan.common.util.CollectCommand
import eu.noharmdan.common.util.collectState
import eu.noharmdan.common.util.getOnEvent
import eu.noharmdan.data.R
import eu.noharmdan.showcase.base.BaseComposeActivity
import eu.noharmdan.showcase.scene.quiz.model.placeholderQuestion
import eu.noharmdan.showcase.scene.quiz.screen.ErrorScreen
import eu.noharmdan.showcase.scene.quiz.screen.IntroductionScreen
import eu.noharmdan.showcase.scene.quiz.screen.LoadingScreen
import eu.noharmdan.showcase.scene.quiz.screen.QuestionScreen
import eu.noharmdan.showcase.scene.quiz.screen.WrongAnswerScreen
import eu.noharmdan.showcase.ui.theme.ShowcaseTheme
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.getViewModel

/**
 * A convenience type alias for the [QuizViewEvent] lambda.
 */
typealias OnEvent = (QuizViewEvent) -> Unit

/**
 * The main quiz activity class which uses [QuizScreen] as its
 * full screen composable.
 */
class QuizActivity : BaseComposeActivity() {

    @Composable
    override fun RootCompose() {
        QuizScreen(onQuitSelected = {
            finish()
        })
    }
}

/**
 * The main screen composable which sets up the [QuizViewModel]
 * and communication with it. The layout is provided through
 * the [Content] composable. Also manages the [QuitConfirmationDialog],
 * [CommandProcessor] and a [BackHandler].
 *
 * @param onQuitSelected to be called when the activity should close
 * by a request of the user.
 *
 * *This function is kept outside of the activity class body
 * as a good practice that prevents accessing any of the
 * activity fields or functions which are not meant to be
 * used in compose and would potentially trigger recompositions
 * or behavior duplicates to that provided be their composable
 * counterparts.*
 */
@Composable
private fun QuizScreen(onQuitSelected: () -> Unit) {
    val viewModel = getViewModel<QuizViewModel>()
    val viewState = viewModel.collectState()
    val onEvent = viewModel.getOnEvent()

    val quitConfirmationDialogState = remember { mutableStateOf(value = false) }

    Content(
        viewState = viewState,
        onEvent = onEvent
    )

    QuitConfirmationDialog(
        showDialog = quitConfirmationDialogState,
        onConfirmed = onQuitSelected
    )

    CommandProcessor(
        viewModel = viewModel,
        quitConfirmationDialogState = quitConfirmationDialogState,
        onQuitSelected = onQuitSelected
    )

    BackHandler {
        onEvent(QuizViewEvent.OnBackPressed)
    }
}

/**
 * The composable content of the entire quiz scene.
 *
 * Switches between different screens based on the current
 * [viewState], providing them with as little data from it
 * as possible in order to avoid unnecessary recompositions,
 * as well as the [onEvent] callback for view model communication.
 *
 * Kept separate from [QuizScreen] to enable using previews.
 */
@Composable
private fun Content(viewState: QuizViewState, onEvent: OnEvent) {
    /*
     * Scaffold is actually not necessary if it only has content declared,
     * that is however not the case in most applications as e.g. toolbars
     * or bottom navigation is often present.
     */
    Scaffold(
        content = { paddingValues ->
            val modifier = Modifier.padding(paddingValues = paddingValues)

            when (viewState.state) {
                QuizViewState.State.Loading -> LoadingScreen(
                    modifier = modifier
                )

                QuizViewState.State.Error -> ErrorScreen(
                    modifier = modifier,
                    onEvent = onEvent
                )

                QuizViewState.State.Introduction -> IntroductionScreen(
                    modifier = modifier,
                    highScore = viewState.highScore,
                    onEvent = onEvent
                )

                is QuizViewState.State.Questions -> {
                    viewState.state.currentQuestion?.let {
                        QuestionScreen(
                            modifier = modifier,
                            highScore = viewState.highScore,
                            currentScore = viewState.currentScore,
                            currentQuestion = it,
                            onEvent = onEvent
                        )
                    } ?: ErrorScreen(
                        modifier = modifier,
                        onEvent = onEvent
                    )
                }

                is QuizViewState.State.WrongAnswer -> WrongAnswerScreen(
                    modifier = modifier,
                    highScore = viewState.highScore,
                    currentScore = viewState.currentScore,
                    question = viewState.state.question,
                    onEvent = onEvent
                )
            }
        }
    )
}

/**
 * Shows a simple alert dialog allowing the user to confirm whether they
 * want to quite the application.
 *
 * @param showDialog whether the dialog should be shown (is reset by the
 * dialog when the dialog is closed by the user)
 * @param onConfirmed called when the user confirms their intention to quit
 */
@Composable
fun QuitConfirmationDialog(
    showDialog: MutableState<Boolean>,
    onConfirmed: () -> Unit
) {
    ConfirmationDialog(
        showDialog = showDialog,
        title = stringResource(id = R.string.are_you_sure),
        text = stringResource(id = R.string.quit_dialog_text),
        confirmString = stringResource(id = R.string.quit),
        onConfirmClicked = onConfirmed,
        cancelString = stringResource(id = R.string.cancel)
    )
}

/**
 * Collects the UI commands from the [viewModel], changing the value
 * of [quitConfirmationDialogState] to `true` if the dialog is to be
 * shown, or calling [onQuitSelected] if the application should close.
 */
@Composable
fun CommandProcessor(
    viewModel: QuizViewModel,
    quitConfirmationDialogState: MutableState<Boolean>,
    onQuitSelected: () -> Unit
) {
    CollectCommand(viewModel = viewModel) { command ->
        when (command) {
            QuizViewCommand.ShowQuitConfirmationDialog -> quitConfirmationDialogState.value = true
            QuizViewCommand.Quit -> onQuitSelected()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {
    ShowcaseTheme {
        Content(
            viewState = QuizViewState(
                state = QuizViewState.State.Questions(
                    questions = persistentListOf(placeholderQuestion),
                    currentQuestionIndex = 0
                )
            ),
            onEvent = {}
        )
    }
}

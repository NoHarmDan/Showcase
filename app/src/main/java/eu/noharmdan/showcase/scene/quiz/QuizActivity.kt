package eu.noharmdan.showcase.scene.quiz

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.noharmdan.common.util.collectState
import eu.noharmdan.common.util.getOnEvent
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
        QuizScreen()
    }
}

/**
 * The main screen composable which sets up the [QuizViewModel]
 * and communication with it. The layout is provided through
 * the [Content] composable.
 *
 * *This function is kept outside of the activity class body
 * as a good practice that prevents accessing any of the
 * activity fields or functions which are not meant to be
 * used in compose and would potentially trigger recompositions
 * or behavior duplicates to that provided be their composable
 * counterparts.*
 */
@Composable
private fun QuizScreen() {
    val viewModel = getViewModel<QuizViewModel>()
    val viewState = viewModel.collectState()
    val onEvent = viewModel.getOnEvent()

    Content(viewState, onEvent)
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
                is QuizViewState.State.Questions -> QuestionScreen(
                    modifier = modifier,
                    highScore = viewState.highScore,
                    currentScore = viewState.currentScore,
                    currentQuestion = viewState.state.currentQuestion,
                    onEvent = onEvent
                )
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

package eu.noharmdan.showcase.scene.quiz

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.noharmdan.showcase.base.BaseComposeActivity
import eu.noharmdan.showcase.scene.quiz.screen.ErrorScreen
import eu.noharmdan.showcase.scene.quiz.screen.IntroductionScreen
import eu.noharmdan.showcase.scene.quiz.screen.LoadingScreen
import eu.noharmdan.showcase.scene.quiz.screen.QuestionScreen
import eu.noharmdan.showcase.scene.quiz.screen.WrongAnswerScreen
import eu.noharmdan.common.util.collectState
import eu.noharmdan.common.util.getOnEvent
import org.koin.androidx.compose.getViewModel

typealias OnEvent = (QuizViewEvent) -> Unit

class QuizActivity : BaseComposeActivity() {

    @Composable
    override fun RootCompose() {
        QuizScreen()
    }
}

@Composable
fun QuizScreen() {
    val viewModel = getViewModel<QuizViewModel>()
    val viewState = viewModel.collectState()
    val onEvent = viewModel.getOnEvent()

    Content(viewState, onEvent)
}

@Composable
fun Content(viewState: QuizViewState, onEvent: OnEvent) {
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

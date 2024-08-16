package eu.noharmdan.showcase.scene.quiz

import androidx.compose.runtime.Immutable
import eu.noharmdan.showcase.base.ViewState
import eu.noharmdan.showcase.scene.quiz.model.Question
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class QuizViewState(
    val state: State = State.Introduction,
    val highScore: Int = 0,
    val currentScore: Int = 0,
) : ViewState {

    sealed class State {
        data object Loading : State()
        data object Introduction : State()
        data object Error : State()

        @Immutable
        data class Questions(
            val questions: ImmutableList<Question> = persistentListOf(),
            val currentQuestionIndex: Int = 0,
            val isCorrectAnswerSelected: Boolean = false,
        ) : State() {
            val currentQuestion = questions[currentQuestionIndex] // todo out of bounds safety?
        }

        @Immutable
        data class WrongAnswer(
            val question: Question
        ) : State()
    }
}

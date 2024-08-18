package eu.noharmdan.showcase.scene.quiz

import androidx.compose.runtime.Immutable
import eu.noharmdan.common.base.ViewState
import eu.noharmdan.data.model.QuestionCategory
import eu.noharmdan.data.model.QuestionDifficulty
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
            val questions: ImmutableList<QuestionViewState> = persistentListOf(),
            val currentQuestionIndex: Int = 0,
        ) : State() {
            val currentQuestion = questions[currentQuestionIndex] // todo out of bounds safety?
        }

        @Immutable
        data class WrongAnswer(
            val question: QuestionViewState
        ) : State()
    }
}

@Immutable
data class QuestionViewState(
    val category: QuestionCategory,
    val tags: ImmutableList<String>,
    val regions: ImmutableList<String>,
    val difficulty: QuestionDifficulty,
    val isNiche: Boolean,
    val text: String,
    val answers: ImmutableList<AnswerViewState>,
) {

    @Immutable
    data class AnswerViewState(
        val text: String,
        val isCorrect: Boolean,
        val isSelected: Boolean,
    )
}

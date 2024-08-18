package eu.noharmdan.showcase.scene.quiz

import androidx.compose.runtime.Immutable
import eu.noharmdan.common.base.ViewState
import eu.noharmdan.data.model.QuestionCategory
import eu.noharmdan.data.model.QuestionDifficulty
import eu.noharmdan.showcase.scene.quiz.QuizViewState.State
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * The data class subclass of [ViewState] to be used with [QuizViewModel]
 * to represent the current state of the UI as well as hold the data
 * necessary for it.
 *
 * The main possible [state]s achievable are represented by the subclasses
 * of [State]. Each of which has its own composable screen representation.
 */
data class QuizViewState(
    val state: State = State.Introduction,
    val highScore: Int = 0,
    val currentScore: Int = 0,
) : ViewState {

    sealed class State {
        data object Loading : State()
        data object Introduction : State()
        data object Error : State()

        /**
         * This state holds the data necessary for playing the quiz itself.
         *
         * @param questions contains all currently loaded questions
         * @param currentQuestionIndex contains the index of the question
         * currently visible to the user
         *
         * @see currentQuestion
         */
        @Immutable
        data class Questions(
            val questions: ImmutableList<QuestionViewState> = persistentListOf(),
            val currentQuestionIndex: Int = 0,
        ) : State() {

            /**
             * A convenience function to return the [QuestionViewState] of [questions]
             * which is now visible to the user, or `null` if [currentQuestionIndex]
             * is out of bounds.
             */
            val currentQuestion = questions.getOrNull(currentQuestionIndex)
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

package eu.noharmdan.showcase.scene.quiz

import androidx.compose.runtime.Immutable
import eu.noharmdan.common.base.ViewState
import eu.noharmdan.showcase.model.QuestionCategory
import eu.noharmdan.showcase.model.QuestionDifficulty
import eu.noharmdan.showcase.rest.response.QuestionResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

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
        ) : State() {
            val currentQuestion = questions[currentQuestionIndex] // todo out of bounds safety?
        }

        @Immutable
        data class WrongAnswer(
            val question: Question
        ) : State()
    }
}

@Immutable
data class Question(
    val id: String,
    val category: QuestionCategory,
    val tags: ImmutableList<String>,
    val regions: ImmutableList<String>,
    val difficulty: QuestionDifficulty,
    val isNiche: Boolean,
    val text: String,
    val answers: ImmutableList<Answer>,
) {

    @Immutable
    data class Answer(
        val text: String,
        val isCorrect: Boolean,
        val isSelected: Boolean,
    )

    companion object {
        fun fromQuestionResponse(response: QuestionResponse) = Question(
            id = response.id,
            category = response.category,
            tags = response.tags.toImmutableList(),
            regions = response.regions.toImmutableList(),
            difficulty = response.difficulty,
            isNiche = response.isNiche,
            text = response.question.text,
            answers = (
                    listOf(
                        Answer(
                            text = response.correctAnswer,
                            isCorrect = true,
                            isSelected = false,
                        )
                    ) + response.incorrectAnswers.map {
                        Answer(
                            text = it,
                            isCorrect = false,
                            isSelected = false,
                        )
                    }
                    )
                .shuffled()
                .toImmutableList(),
        )
    }
}

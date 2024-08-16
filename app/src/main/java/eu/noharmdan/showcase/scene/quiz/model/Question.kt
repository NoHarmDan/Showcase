package eu.noharmdan.showcase.scene.quiz.model

import androidx.compose.runtime.Immutable
import eu.noharmdan.showcase.model.QuestionCategory
import eu.noharmdan.showcase.model.QuestionDifficulty
import eu.noharmdan.showcase.rest.response.QuestionResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

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
                            isCorrect = true
                        )
                    ) + response.incorrectAnswers.map {
                        Answer(
                            text = it,
                            isCorrect = false
                        )
                    }
                    ).toImmutableList(),
        )
    }
}

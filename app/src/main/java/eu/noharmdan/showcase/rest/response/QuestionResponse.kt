package eu.noharmdan.showcase.rest.response

import eu.noharmdan.data.model.Question
import eu.noharmdan.data.model.QuestionCategory
import eu.noharmdan.data.model.QuestionDifficulty

data class QuestionResponse(
    val id: String,
    val category: QuestionCategory,
    val tags: List<String>,
    val difficulty: QuestionDifficulty,
    val regions: List<String>,
    val isNiche: Boolean,
    val question: QuestionTextResponse,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
) {
    fun toQuestion() = Question(
        id = id,
        category = category,
        tags = tags,
        regions = regions,
        difficulty = difficulty,
        isNiche = isNiche,
        text = question.text,
        answers = (
                listOf(
                    Question.Answer(
                        text = correctAnswer,
                        isCorrect = true,
                    )
                ) + incorrectAnswers.map {
                    Question.Answer(
                        text = it,
                        isCorrect = false,
                    )
                }
                )
            .shuffled()
    )


}

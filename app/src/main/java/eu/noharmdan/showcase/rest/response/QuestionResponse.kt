package eu.noharmdan.showcase.rest.response

import eu.noharmdan.showcase.model.QuestionCategory
import eu.noharmdan.showcase.model.QuestionDifficulty

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
)

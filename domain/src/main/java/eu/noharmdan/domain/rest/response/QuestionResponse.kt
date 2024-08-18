package eu.noharmdan.domain.rest.response

import eu.noharmdan.data.model.QuestionCategory
import eu.noharmdan.data.model.QuestionDifficulty
import eu.noharmdan.domain.util.toQuestion

/**
 * A simple DTO representation of a quiz question exactly as
 * returned by the REST API, including its [QuestionTextResponse].
 *
 * This class should be excluded from obfuscation in order
 * for de/serialization to work.
 *
 * @see toQuestion
 */
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

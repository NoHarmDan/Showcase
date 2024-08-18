package eu.noharmdan.domain.rest.response

/**
 * A simple DTO representation of a quiz question text
 * exactly as returned by the REST API.
 *
 * This class should be excluded from obfuscation in order
 * for de/serialization to work.
 *
 * @see [QuestionResponse]
 */
data class QuestionTextResponse(
    val text: String,
)

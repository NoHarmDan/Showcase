package eu.noharmdan.domain.util

import eu.noharmdan.data.model.Question
import eu.noharmdan.domain.rest.ApiResponse
import eu.noharmdan.domain.rest.RestClient
import eu.noharmdan.domain.rest.response.QuestionResponse
import retrofit2.Response

/**
 * A convenience function for efficient calling of [RestClient] API [call]s.
 *
 * Executes the call and returns one of [ApiResponse] subclasses representing
 * the result of the call or an error state if an exception is thrown or returned
 * from the API.
 */
internal suspend fun <T> RestClient.getResult(call: suspend RestClient.() -> Response<T>): ApiResponse<T> {
    return try {
        val response = call(this)

        if (response.isSuccessful) {
            /*
             * It may be a good idea to judge the success of a response not only by retrofit's fields
             * and the nullability of the body, but also whether the HTTP codes correlate to what is expected,
             * e.g. that a "no content" response actually does carry the 204 code.
             */
            response.body()?.let { body ->
                ApiResponse.Data(response.code(), body)
            } ?: ApiResponse.NoContent(response.code())
        } else {
            ApiResponse.Error(code = response.code())
        }
    } catch (e: Exception) {
        ApiResponse.Error(
            code = -1,
            error = e
        )
    }
}

/**
 * A converter function which returns a new instance of [Question]
 * filled with data as received from a [QuestionResponse].
 *
 * The [Question.answers] are set in random order.
 */
fun QuestionResponse.toQuestion() = Question(
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
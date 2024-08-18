package eu.noharmdan.domain.rest

import eu.noharmdan.data.model.QuestionCategory
import eu.noharmdan.data.model.QuestionDifficulty
import eu.noharmdan.domain.rest.response.QuestionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The REST API calls specification interface, to be implemented by Retrofit
 * and instantiated through [ClientFactory].
 *
 * Defines all REST API calls.
 */
interface RestClient {

    /**
     * Fetches a list of random [QuestionResponse] from the REST API.
     *
     * @param limit the optional maximum number of questions to fetch (default is 10, maximum is 50)
     * @param categories the optional list of [QuestionCategory] to limit the results
     * @param difficulties the optional list of [QuestionDifficulty] to limit the results
     * @param tags the optional list of text tags to limit the results
     */
    @GET("questions")
    @JvmSuppressWildcards
    suspend fun getRandomQuestions(
        @Query("limit") limit: Int? = null,
        @Query("categories") categories: List<QuestionCategory>? = null,
        @Query("difficulties") difficulties: List<QuestionDifficulty>? = null,
        @Query("tags") tags: List<String>? = null,
    ): Response<List<QuestionResponse>>
}
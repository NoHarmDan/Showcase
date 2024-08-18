package eu.noharmdan.domain.rest

import eu.noharmdan.data.model.QuestionCategory
import eu.noharmdan.data.model.QuestionDifficulty
import eu.noharmdan.domain.rest.response.QuestionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestClient {

    @GET("questions")
    @JvmSuppressWildcards
    suspend fun getRandomQuestions(
        @Query("limit") limit: Int? = null,
        @Query("categories") categories: List<QuestionCategory>? = null,
        @Query("difficulties") difficulties: List<QuestionDifficulty>? = null,
        @Query("tags") tags: List<String>? = null,
    ): Response<List<QuestionResponse>>
}
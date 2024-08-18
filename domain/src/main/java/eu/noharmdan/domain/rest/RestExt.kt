package eu.noharmdan.domain.rest

import retrofit2.Response

suspend fun <T> RestClient.getResult(call: suspend RestClient.() -> Response<T>): ApiResponse<T> {
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
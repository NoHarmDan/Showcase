package eu.noharmdan.domain.rest

/**
 * A sealed class representing all possible forms of
 * a [RestClient] call response.
 *
 * [code] is the HTTP code of the response.
 *
 * - [Data] for a successful response with a deserializable body
 * - [NoContent] for a successful response without a body
 * - [Error] for an unsuccessful response or an exception during the call
 *
 * Both [Data] and [NoContent] are superclassed by [Success], which may
 * be used e.g. in comparisons that do not care what kind of response was
 * received, only if the call was successful.
 */
internal sealed class ApiResponse<out T>(
    val code: Int
) {

    abstract class Success<T>(
        code: Int
    ) : ApiResponse<T>(code)

    class Data<T>(
        code: Int,
        val data: T
    ) : Success<T>(code)

    class NoContent<T>(
        code: Int
    ) : Success<T>(code)

    class Error<T>(
        code: Int,
        val data: T? = null, // May be used with a more complex API call with e.g. custom error codes and additional information.
        val error: Throwable? = null,
    ) : ApiResponse<T>(code)
}
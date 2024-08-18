package eu.noharmdan.domain.rest

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
        val data: T? = null,
        val error: Throwable? = null,
    ) : ApiResponse<T>(code)
}
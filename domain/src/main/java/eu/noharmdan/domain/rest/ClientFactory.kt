package eu.noharmdan.domain.rest

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import eu.noharmdan.data.model.QuestionCategory
import eu.noharmdan.data.model.QuestionDifficulty
import eu.noharmdan.domain.rest.converter.QuestionCategoryConverter
import eu.noharmdan.domain.rest.converter.QuestionDifficultyConverter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * A factory used to setup and create a [RestClient] instance connected to [BASE_URL].
 *
 * *In an application which may not access the REST API in some scenarios,
 * this factory could alternatively be declared e.g. as a class that only
 * gets instantiated through dependency injection if needed. That is however
 * not the case for this application.*
 *
 * @see createClient
 */
internal object ClientFactory {
    private const val TAG = "OkHttp"
    private const val BASE_URL = "https://the-trivia-api.com/v2/"
    private const val TIMEOUT_SECONDS_DEFAULT = 10L

    /**
     * The [Gson] instance used to parse response for console logging
     * by [loggingInterceptor].
     *
     * Sets a human-readable "prettified" output.
     */
    private val loggingGson by lazy {
        GsonBuilder()
            .setPrettyPrinting()
            .create()
    }

    /**
     * The [Gson] instance used to parse message bodies in REST communication.
     *
     * Set the type adapters for [QuestionCategory] and [QuestionDifficulty].
     */
    private val restGson by lazy {
        GsonBuilder()
            .registerTypeAdapter(QuestionCategory::class.java, QuestionCategoryConverter())
            .registerTypeAdapter(QuestionDifficulty::class.java, QuestionDifficultyConverter())
            .create()
    }

    /**
     * An interceptor which prints out the REST message header and body
     * line-by-line in a human-readable prettified format using [loggingGson].
     */
    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor { message ->
            if (message.startsWith("{") || message.startsWith("[")) {
                try {
                    val prettyPrintJson = loggingGson.toJson(JsonParser().parse(message))
                    prettyPrintJson.lines().forEach { line ->
                        Log.i(TAG, line)
                    }
                } catch (m: JsonSyntaxException) {
                    Log.i(TAG, message)
                }
            } else {
                Log.i(TAG, message)
            }
        }.setLevel(
            HttpLoggingInterceptor.Level.BODY
        )
    }

    /**
     * Sets up a [RestClient] with the given [timeoutSeconds], [loggingInterceptor],
     * JSON parsing through [restGson] and [BASE_URL] and returns its
     * ready-to-use instance.
     */
    fun createClient(timeoutSeconds: Long = TIMEOUT_SECONDS_DEFAULT): RestClient {
        val client = OkHttpClient.Builder()
            .callTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(restGson))
            .client(client)
            .build()

        return retrofit.create(RestClient::class.java)
    }

}
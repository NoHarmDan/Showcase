package eu.noharmdan.showcase.rest

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import eu.noharmdan.data.model.QuestionCategory
import eu.noharmdan.data.model.QuestionDifficulty
import eu.noharmdan.showcase.rest.converter.QuestionCategoryConverter
import eu.noharmdan.showcase.rest.converter.QuestionDifficultyConverter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit

object ClientFactory {
    private const val TAG = "OkHttp"
    private const val BASE_URL = "https://the-trivia-api.com/v2/"
    private const val TIMEOUT_DEFAULT = 10L

    private val loggingGson by lazy {
        GsonBuilder()
            .setPrettyPrinting()
            .create()
    }

    private val responseGson by lazy {
        GsonBuilder()
            .registerTypeAdapter(QuestionCategory::class.java, QuestionCategoryConverter())
            .registerTypeAdapter(QuestionDifficulty::class.java, QuestionDifficultyConverter())
            .create()
    }

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

    fun createClient(timeout: Long = TIMEOUT_DEFAULT): RestClient {
        val client = OkHttpClient.Builder()
            .callTimeout(timeout, TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(responseGson))
            .client(client)
            .build()

        return retrofit.create(RestClient::class.java)
    }

}
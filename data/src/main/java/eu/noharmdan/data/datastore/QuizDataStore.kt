package eu.noharmdan.data.datastore

import android.content.Context
import eu.noharmdan.common.base.BaseDataStore

/**
 * The data store for all quiz-related data persistence.
 *
 * @see highScore
 */
class QuizDataStore(context: Context) : BaseDataStore(context, NAME) {

    /**
     * The latest high score scored in the quiz or 0 if none was stored yet.
     *
     * @see setHighScore
     */
    val highScore by lazyFlow(keyHighScore, defaultValue = { 0 })

    /**
     * The function to store the latest high score.
     *
     * @see highScore
     */
    suspend fun setHighScore(highScore: Int) = set(keyHighScore, highScore)

    companion object {
        const val NAME = "app_preferences"
    }
}
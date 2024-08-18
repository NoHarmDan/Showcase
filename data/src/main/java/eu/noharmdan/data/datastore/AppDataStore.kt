package eu.noharmdan.data.datastore

import android.content.Context
import eu.noharmdan.common.base.BaseDataStore

class AppDataStore(context: Context) : BaseDataStore(context, NAME) {

    val highScore by lazyFlow(keyHighScore, defaultValue = { 0 })
    suspend fun setHighScore(highScore: Int) = set(keyHighScore, highScore)

    companion object {
        const val NAME = "app_preferences"
    }
}
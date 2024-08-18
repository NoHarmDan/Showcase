package eu.noharmdan.data.datastore

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.Preferences

/**
 * The [Preferences.Key] used to store the high score in [QuizDataStore].
 */
internal val keyHighScore = intPreferencesKey(name = "high_score")
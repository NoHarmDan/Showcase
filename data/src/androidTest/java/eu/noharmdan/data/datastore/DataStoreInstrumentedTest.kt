package eu.noharmdan.data.datastore

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStoreInstrumentedTest {

    @Test
    fun scorePersistence_isCorrect() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dataStore = QuizDataStore(context)

        val score = 17

        dataStore.setHighScore(score)

        val persisted = dataStore.highScore.first()

        assertEquals(score, persisted)
    }
}
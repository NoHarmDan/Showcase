package eu.noharmdan.data.datastore

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class DataStoreInstrumentedTest {

    /**
     * A simple test for the [QuizDataStore] score storage.
     *
     * Tests whether a correct value is available through [QuizDataStore.highScore]
     * right after being stored through [QuizDataStore.setHighScore].
     */
    @Test
    fun scorePersistence_isCorrect() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val dataStore = QuizDataStore(context)

        val score = Random.nextInt()

        dataStore.setHighScore(score)

        val persisted = dataStore.highScore.first()

        assertEquals(score, persisted)
    }
}
package eu.noharmdan.domain.rest.converter

import com.google.gson.JsonPrimitive
import eu.noharmdan.data.model.QuestionCategory
import eu.noharmdan.data.model.QuestionDifficulty
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * A simple set of functions to test [QuestionCategory] and [QuestionDifficulty] serialization and deserialization,
 * assuming the serialized value is the [QuestionCategory.id] or [QuestionDifficulty.id].
 */
class QuestionConvertersUnitTest {

    @Test
    fun categorySerialization_isCorrect() {
        val category = QuestionCategory.entries.random()

        val serialized = QuestionCategoryConverter().serialize(
            src = category,
            typeOfSrc = null,
            context = null
        )

        assertEquals(category.id, serialized.asString)
    }

    @Test
    fun categoryDeserialization_isCorrect() {
        val serialized = QuestionCategory.entries.random().id

        val category = QuestionCategoryConverter().deserialize(
            json = JsonPrimitive(serialized),
            typeOfT = QuestionCategory::class.java,
            context = null
        )

        assertEquals(serialized, category.id)
    }

    @Test
    fun difficultySerialization_isCorrect() {
        val difficulty = QuestionDifficulty.entries.random()

        val serialized = QuestionDifficultyConverter().serialize(
            src = difficulty,
            typeOfSrc = null,
            context = null
        )

        assertEquals(difficulty.id, serialized.asString)
    }

    @Test
    fun difficultyDeserialization_isCorrect() {
        val serialized = QuestionDifficulty.entries.random().id

        val difficulty = QuestionDifficultyConverter().deserialize(
            json = JsonPrimitive(serialized),
            typeOfT = QuestionCategory::class.java,
            context = null
        )

        assertEquals(serialized, difficulty.id)
    }

}
package eu.noharmdan.showcase.rest.converter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import eu.noharmdan.data.model.QuestionDifficulty
import java.lang.reflect.Type

class QuestionDifficultyConverter : JsonDeserializer<QuestionDifficulty?>, JsonSerializer<QuestionDifficulty?> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): QuestionDifficulty {
        return QuestionDifficulty.getById(json.asString)
    }

    override fun serialize(src: QuestionDifficulty?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.id)
    }
}
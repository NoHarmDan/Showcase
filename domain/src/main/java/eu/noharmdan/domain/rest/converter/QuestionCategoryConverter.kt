package eu.noharmdan.domain.rest.converter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import eu.noharmdan.data.model.QuestionCategory
import java.lang.reflect.Type

internal class QuestionCategoryConverter : JsonDeserializer<QuestionCategory?>, JsonSerializer<QuestionCategory?> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): QuestionCategory {
        return QuestionCategory.getById(json.asString)
    }

    override fun serialize(src: QuestionCategory?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.id)
    }
}
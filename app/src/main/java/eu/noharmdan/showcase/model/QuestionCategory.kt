package eu.noharmdan.showcase.model

import androidx.annotation.StringRes
import eu.noharmdan.showcase.R

enum class QuestionCategory(val id: String, @StringRes val titleResId: Int) {
    Unknown(id = "unknown", titleResId = R.string.category_unknown),
    Music(id = "music", titleResId = R.string.category_music),
    SportAndLeisure(id = "sport_and_leisure", titleResId = R.string.category_sport_and_leisure),
    FilmAndTv(id = "film_and_tv", titleResId = R.string.category_film_and_tv),
    ArtsAndLiterature(id = "arts_and_literature", titleResId = R.string.category_arts_and_literature),
    History(id = "history", titleResId = R.string.category_history),
    SocietyAndCulture(id = "society_and_culture", titleResId = R.string.category_society_and_culture),
    Science(id = "science", titleResId = R.string.category_science),
    Geography(id = "geography", titleResId = R.string.category_geography),
    FoodAndDrink(id = "food_and_drink", titleResId = R.string.category_food_and_drink),
    GeneralKnowledge(id = "general_knowledge", titleResId = R.string.category_general_knowledge);

    companion object {
        private val valuesById by lazy {
            entries.associateBy { it.id }
        }

        fun getById(id: String) = valuesById[id] ?: Unknown
    }
}

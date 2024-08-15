package eu.noharmdan.showcase.model

import androidx.annotation.StringRes
import eu.noharmdan.showcase.R

enum class QuestionCategory(val id: String, @StringRes val titleResId: Int) {
    unknown(id = "unknown", titleResId = R.string.category_unknown),
    music(id = "music", titleResId = R.string.category_music),
    sportAndLeisure(id = "sport_and_leisure", titleResId = R.string.category_sport_and_leisure),
    filmAndTv(id = "film_and_tv", titleResId = R.string.category_film_and_tv),
    artsAndLiterature(id = "arts_and_literature", titleResId = R.string.category_arts_and_literature),
    history(id = "history", titleResId = R.string.category_history),
    societyAndCulture(id = "society_and_culture", titleResId = R.string.category_society_and_culture),
    science(id = "science", titleResId = R.string.category_science),
    geography(id = "geography", titleResId = R.string.category_geography),
    foodAndDrink(id = "food_and_drink", titleResId = R.string.category_food_and_drink),
    generalKnowledge(id = "general_knowledge", titleResId = R.string.category_general_knowledge);

    companion object {
        private val valuesById by lazy {
            entries.associateBy { it.id }
        }

        fun getById(id: String) = valuesById[id] ?: unknown
    }
}

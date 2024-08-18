package eu.noharmdan.data.model

import androidx.annotation.StringRes
import eu.noharmdan.data.R

/**
 * A enum representation of the known [Question] categories, as well as [Unknown]
 * in case the provided data differs from expected.
 *
 * All categories are identified by their [id], which is the text representation
 * provided by the REST API. This value must not be changed.
 *
 * If a category is meant to be shown to a user in a readable format, its [titleResId]
 * is meant to be used.
 *
 * @see valuesById
 * @see getById
 */
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

        /**
         * A constant but lazy-initialized map containing all categories
         * keyed by their [id].
         *
         * @see getById
         */
        private val valuesById by lazy {
            entries.associateBy { it.id }
        }

        /**
         * A convenience function which finds the correct [QuestionCategory]
         * for a given [id] in [valuesById], or [Unknown] if none is found.
         */
        fun getById(id: String) = valuesById[id] ?: Unknown
    }
}

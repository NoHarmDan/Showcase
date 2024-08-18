package eu.noharmdan.data.model

/**
 * A enum representation of the known [Question] difficulties, as well as [Unknown]
 * in case the provided data differs from expected.
 *
 * All difficulties are identified by their [id], which is the text representation
 * provided by the REST API. This value must not be changed.
 *
 * @see valuesById
 * @see getById
 */
enum class QuestionDifficulty(val id: String) {
    Unknown(id = "unknown"),
    Easy(id = "easy"),
    Medium(id = "medium"),
    Hard(id = "hard");

    companion object {

        /**
         * A constant but lazy-initialized map containing all difficulties
         * keyed by their [id].
         *
         * @see getById
         */
        private val valuesById by lazy {
            entries.associateBy { it.id }
        }

        /**
         * A convenience function which finds the correct [QuestionDifficulty]
         * for a given [id] in [valuesById], or [Unknown] if none is found.
         */
        fun getById(id: String) = valuesById[id] ?: Unknown
    }
}
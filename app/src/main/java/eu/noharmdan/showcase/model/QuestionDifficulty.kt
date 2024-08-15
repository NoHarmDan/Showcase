package eu.noharmdan.showcase.model

enum class QuestionDifficulty(val id: String) {
    unknown(id = "unknown"),
    easy(id = "easy"),
    medium(id = "medium"),
    hard(id = "hard");

    companion object {
        private val valuesById by lazy {
            entries.associateBy { it.id }
        }

        fun getById(id: String) = valuesById[id] ?: unknown
    }
}
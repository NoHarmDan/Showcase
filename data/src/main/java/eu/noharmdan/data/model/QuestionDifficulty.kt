package eu.noharmdan.data.model

enum class QuestionDifficulty(val id: String) {
    Unknown(id = "unknown"),
    Easy(id = "easy"),
    Medium(id = "medium"),
    Hard(id = "hard");

    companion object {
        private val valuesById by lazy {
            entries.associateBy { it.id }
        }

        fun getById(id: String) = valuesById[id] ?: Unknown
    }
}
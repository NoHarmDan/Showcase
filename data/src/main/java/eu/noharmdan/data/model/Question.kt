package eu.noharmdan.data.model

data class Question(
    val id: String,
    val category: QuestionCategory,
    val tags: List<String>,
    val regions: List<String>,
    val difficulty: QuestionDifficulty,
    val isNiche: Boolean,
    val text: String,
    val answers: List<Answer>,
) {

    data class Answer(
        val text: String,
        val isCorrect: Boolean,
    )
}

package eu.noharmdan.data.model

/**
 * A simple DTO representing a text quiz question with it's [Answer]s.
 *
 * It is assumed that there are always four answers to a question,
 * but no uses of [answers] should rely on that information.
 */
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

    /**
     * A simple DTO representing a text quiz answer and whether it is correct.
     */
    data class Answer(
        val text: String,
        val isCorrect: Boolean,
    )
}

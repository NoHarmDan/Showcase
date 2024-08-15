package eu.noharmdan.showcase.model

import eu.noharmdan.showcase.rest.response.QuestionResponse

// todo maybe mention that dto in "model" doesn't necessarily make sense and would make more sense if it was persisted etc.
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

    companion object {
        fun fromQuestionResponse(response: QuestionResponse) = Question(
            id = response.id,
            category = response.category,
            tags = response.tags,
            regions = response.regions,
            difficulty = response.difficulty,
            isNiche = response.isNiche,
            text = response.question.text,
            answers = listOf(
                Answer(
                    text = response.correctAnswer,
                    isCorrect = true
                )
            ) + response.incorrectAnswers.map {
                Answer(
                    text = it,
                    isCorrect = false
                )
            },
        )
    }
}

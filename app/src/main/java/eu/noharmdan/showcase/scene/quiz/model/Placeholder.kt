package eu.noharmdan.showcase.scene.quiz.model

import eu.noharmdan.showcase.model.QuestionCategory
import eu.noharmdan.showcase.model.QuestionDifficulty
import kotlinx.collections.immutable.persistentListOf

val placeholderQuestion = Question(
    id = "some_id",
    category = QuestionCategory.science,
    tags = persistentListOf("a_tag"),
    regions = persistentListOf(),
    difficulty = QuestionDifficulty.medium,
    isNiche = true,
    text = "What is the airspeed velocity of an unladen swallow?",
    answers = persistentListOf(
        Question.Answer(
            text = "8 mps",
            isCorrect = false
        ),
        Question.Answer(
            text = "11 mps",
            isCorrect = false
        ),
        Question.Answer(
            text = "14 mps",
            isCorrect = false
        ),
        Question.Answer(
            text = "An African or European swallow?",
            isCorrect = true
        ),
    )
)
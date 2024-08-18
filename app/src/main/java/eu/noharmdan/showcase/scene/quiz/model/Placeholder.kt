package eu.noharmdan.showcase.scene.quiz.model

import eu.noharmdan.showcase.model.QuestionCategory
import eu.noharmdan.showcase.model.QuestionDifficulty
import eu.noharmdan.showcase.scene.quiz.Question
import kotlinx.collections.immutable.persistentListOf

val placeholderQuestion = Question(
    id = "some_id",
    category = QuestionCategory.Science,
    tags = persistentListOf("a_tag"),
    regions = persistentListOf(),
    difficulty = QuestionDifficulty.Medium,
    isNiche = true,
    text = "What is the airspeed velocity of an unladen swallow?",
    answers = persistentListOf(
        Question.Answer(
            text = "8 mps",
            isCorrect = false,
            isSelected = false,
        ),
        Question.Answer(
            text = "11 mps",
            isCorrect = false,
            isSelected = false,
        ),
        Question.Answer(
            text = "14 mps",
            isCorrect = false,
            isSelected = false,
        ),
        Question.Answer(
            text = "An African or European swallow?",
            isCorrect = true,
            isSelected = true,
        ),
    )
)
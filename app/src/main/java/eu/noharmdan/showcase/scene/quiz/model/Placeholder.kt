package eu.noharmdan.showcase.scene.quiz.model

import eu.noharmdan.data.model.QuestionCategory
import eu.noharmdan.data.model.QuestionDifficulty
import eu.noharmdan.showcase.scene.quiz.QuestionViewState
import kotlinx.collections.immutable.persistentListOf

/**
 * A placeholder instance of [QuestionViewState] to be used in previews etc.
 */
val placeholderQuestion = QuestionViewState(
    category = QuestionCategory.Science,
    tags = persistentListOf("a_tag"),
    regions = persistentListOf(),
    difficulty = QuestionDifficulty.Medium,
    isNiche = true,
    text = "What is the airspeed velocity of an unladen swallow?",
    answers = persistentListOf(
        QuestionViewState.AnswerViewState(
            text = "8 mps",
            isCorrect = false,
            isSelected = false,
        ),
        QuestionViewState.AnswerViewState(
            text = "11 mps",
            isCorrect = false,
            isSelected = false,
        ),
        QuestionViewState.AnswerViewState(
            text = "14 mps",
            isCorrect = false,
            isSelected = false,
        ),
        QuestionViewState.AnswerViewState(
            text = "An African or European swallow?",
            isCorrect = true,
            isSelected = true,
        ),
    )
)
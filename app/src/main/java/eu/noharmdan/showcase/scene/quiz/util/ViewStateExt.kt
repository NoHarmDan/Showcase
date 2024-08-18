package eu.noharmdan.showcase.scene.quiz.util

import eu.noharmdan.common.util.replace
import eu.noharmdan.data.model.Question
import eu.noharmdan.showcase.scene.quiz.QuestionViewState
import eu.noharmdan.showcase.scene.quiz.QuestionViewState.AnswerViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * A converter function which returns a new instance of [QuestionViewState]
 * filled with data as provided in a [Question], transforming all lists to
 * [ImmutableList]s.
 *
 * All [AnswerViewState]s have their [AnswerViewState.isSelected] set to false.
 */
internal fun Question.toQuestionViewState() = QuestionViewState(
    category = category,
    tags = tags.toImmutableList(),
    regions = regions.toImmutableList(),
    difficulty = difficulty,
    isNiche = isNiche,
    text = text,
    answers = answers
        .map { answer ->
            AnswerViewState(
                text = answer.text,
                isCorrect = answer.isCorrect,
                isSelected = false
            )
        }
        .toImmutableList(),
)

/**
 * A convenience reusable function that provides a copy of an [ImmutableList]
 * if [QuestionViewState], where the given [answer] has its [AnswerViewState.isSelected]
 * set to [isSelected].
 */
internal fun ImmutableList<QuestionViewState>.withAnswerSetSelected(
    currentQuestion: QuestionViewState,
    answer: AnswerViewState,
    isSelected: Boolean
): ImmutableList<QuestionViewState> {
    return replace(
        currentQuestion,
        currentQuestion.copy(
            answers = currentQuestion.answers.replace(
                answer,
                answer.copy(isSelected = isSelected)
            ).toImmutableList()
        )
    ).toImmutableList()
}
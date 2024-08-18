package eu.noharmdan.showcase.scene.quiz

import eu.noharmdan.common.base.ViewEvent

/**
 * The sealed subclass of [ViewEvent] to be used with [QuizViewModel],
 * its subclasses representing one-time UI to view model events.
 */
sealed class QuizViewEvent : ViewEvent {
    data object OnStartQuizSelected : QuizViewEvent()
    data object OnTryAgainSelected : QuizViewEvent()
    data class OnAnswerSelected(val answer: QuestionViewState.AnswerViewState) : QuizViewEvent()
}
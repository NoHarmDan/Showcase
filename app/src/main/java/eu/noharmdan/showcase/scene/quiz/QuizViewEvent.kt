package eu.noharmdan.showcase.scene.quiz

import eu.noharmdan.showcase.base.ViewEvent

sealed class QuizViewEvent : ViewEvent {
    data object OnStartQuizSelected : QuizViewEvent()
    data object OnTryAgainSelected : QuizViewEvent()
    data class OnAnswerSelected(val answer: Question.Answer) : QuizViewEvent()
}
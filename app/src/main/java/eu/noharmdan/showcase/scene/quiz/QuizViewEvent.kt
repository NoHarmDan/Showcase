package eu.noharmdan.showcase.scene.quiz

import eu.noharmdan.showcase.base.ViewEvent

sealed class QuizViewEvent : ViewEvent {
    data object StartQuiz : QuizViewEvent()
}
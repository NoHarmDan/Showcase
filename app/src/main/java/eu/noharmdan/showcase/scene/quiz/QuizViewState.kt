package eu.noharmdan.showcase.scene.quiz

import eu.noharmdan.showcase.base.ViewState
import eu.noharmdan.showcase.model.Question
import kotlinx.collections.immutable.ImmutableList

data class QuizViewState(
    val questions: ImmutableList<Question>? = null

    // todo if loading, if starting, if questions answered etc...
) : ViewState

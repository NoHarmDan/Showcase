package eu.noharmdan.showcase.scene.quiz

import android.app.Application
import eu.noharmdan.showcase.base.BaseViewModel
import eu.noharmdan.showcase.base.ViewCommand
import eu.noharmdan.showcase.usecase.GetRandomQuestionsUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class QuizViewModel(application: Application) : BaseViewModel<QuizViewState, QuizViewEvent, ViewCommand>(application, QuizViewState()) {

    private val getRandomQuestionsUseCase: GetRandomQuestionsUseCase by inject()

    override fun onEvent(event: QuizViewEvent) {
        when (event) {
            QuizViewEvent.StartQuiz -> getRandomQuestions()
        }
    }

    private fun getRandomQuestions() {
        ioScope.launch {
            getRandomQuestionsUseCase.execute(
                params = GetRandomQuestionsUseCase.GetRandomQuestionsParams(limit = 20)
            ).collect { questions ->
                updateState {
                    copy(questions = questions?.toImmutableList())
                }
            }
        }
    }
}
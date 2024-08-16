package eu.noharmdan.showcase.scene.quiz

import android.app.Application
import eu.noharmdan.showcase.base.BaseViewModel
import eu.noharmdan.showcase.model.datastore.AppDataStore
import eu.noharmdan.showcase.scene.quiz.model.Question
import eu.noharmdan.showcase.usecase.GetRandomQuestionsUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.inject

class QuizViewModel(application: Application, private val appDataStore: AppDataStore) : BaseViewModel<QuizViewState, QuizViewEvent, QuizViewCommand>(application, QuizViewState()) {

    private val getRandomQuestionsUseCase: GetRandomQuestionsUseCase by inject()

    init {
        appDataStore.highScore.onEach { highScore ->
            updateState {
                copy(highScore = highScore)
            }
        }.launchIn(ioScope)
    }

    override fun onEvent(event: QuizViewEvent) {
        when (event) {
            QuizViewEvent.OnStartQuizSelected -> getRandomQuestions(resetScore = true)
            QuizViewEvent.OnTryAgainSelected -> getRandomQuestions(resetScore = false)
            is QuizViewEvent.OnAnswerSelected -> onAnswerSelected(event.answer)
        }
    }

    private fun getRandomQuestions(resetScore: Boolean) {
        ioScope.launch {
            updateState {
                copy(
                    state = QuizViewState.State.Loading,
                    currentScore = if (resetScore) 0 else currentScore,
                )
            }

            getRandomQuestionsUseCase.execute(
                params = GetRandomQuestionsUseCase.GetRandomQuestionsParams(limit = QUESTIONS_LIMIT)
            ).collect { questions ->
                updateState {
                    val nextState = if (questions == null) {
                        QuizViewState.State.Error
                    } else {
                        QuizViewState.State.Questions(
                            questions = questions.toImmutableList(),
                            currentQuestionIndex = 0
                        )
                    }

                    copy(
                        state = nextState
                    )
                }
            }
        }
    }

    private fun onAnswerSelected(answer: Question.Answer) {
        with(currentState()) {
            defaultScope.launch {
                if (answer.isCorrect && state is QuizViewState.State.Questions) {
                    val score = currentScore + 1
                    val nextQuestionIndex = state.currentQuestionIndex + 1

                    if (nextQuestionIndex == QUESTIONS_LIMIT) {
                        updateState {
                            copy(
                                currentScore = score
                            )
                        }

                        getRandomQuestions(resetScore = false)
                    } else {
                        updateState {
                            copy(
                                currentScore = score,
                                state = (state as? QuizViewState.State.Questions)?.copy(
                                    /*
                                     * Double-checking for type could be avoided if the whole block was wrapped
                                     * with updateState rather than with(currentState()), but this way is more
                                     * concise, readable and more resilient against changes that could cause
                                     * synchronization issues, e.g. with calling getRandomQuestions().
                                     */
                                    currentQuestionIndex = nextQuestionIndex
                                ) ?: QuizViewState.State.Error
                            )
                        }
                    }
                } else {
                    if (currentScore > highScore) {
                        withContext(Dispatchers.IO) {
                            appDataStore.setHighScore(highScore = currentScore)
                        }
                    }

                    updateState {
                        copy(
                            state = (state as? QuizViewState.State.Questions)?.currentQuestion?.let { question ->
                                QuizViewState.State.WrongAnswer(question = question)
                            } ?: QuizViewState.State.Error // This should never happen, but better safe than sorry
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val QUESTIONS_LIMIT = 5
    }
}
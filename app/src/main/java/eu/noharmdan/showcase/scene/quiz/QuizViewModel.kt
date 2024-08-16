package eu.noharmdan.showcase.scene.quiz

import android.app.Application
import eu.noharmdan.showcase.base.BaseViewModel
import eu.noharmdan.showcase.model.datastore.AppDataStore
import eu.noharmdan.showcase.scene.quiz.model.Question
import eu.noharmdan.showcase.usecase.GetRandomQuestionsUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
                if (state !is QuizViewState.State.Questions || state.isCorrectAnswerSelected) {
                    return@launch
                }

                if (answer.isCorrect) {
                    onCorrectAnswerSelected(state)
                } else {
                    onWrongAnswerSelected(state)
                }
            }
        }
    }

    private suspend fun QuizViewState.onCorrectAnswerSelected(questionsState: QuizViewState.State.Questions) {
        updateState {
            copy(
                state = questionsState.copy(
                    isCorrectAnswerSelected = true
                )
            )
        }

        delay(timeMillis = NEXT_QUESTION_DELAY)

        val score = currentScore + 1
        val nextQuestionIndex = questionsState.currentQuestionIndex + 1

        if (nextQuestionIndex == QUESTIONS_LIMIT) {
            updateState {
                copy(
                    currentScore = score,
                    state = questionsState.copy(
                        isCorrectAnswerSelected = false
                    )
                )
            }

            getRandomQuestions(resetScore = false)
        } else {
            updateState {
                copy(
                    currentScore = score,
                    state = questionsState.copy(
                        currentQuestionIndex = nextQuestionIndex,
                        isCorrectAnswerSelected = false
                    )
                )
            }
        }
    }

    private suspend fun QuizViewState.onWrongAnswerSelected(state: QuizViewState.State.Questions) {
        if (currentScore > highScore) {
            withContext(Dispatchers.IO) {
                appDataStore.setHighScore(highScore = currentScore)
            }
        }

        updateState {
            copy(
                state = QuizViewState.State.WrongAnswer(question = state.currentQuestion)
            )
        }
    }

    companion object {
        const val QUESTIONS_LIMIT = 5
        const val NEXT_QUESTION_DELAY = 800L
    }
}
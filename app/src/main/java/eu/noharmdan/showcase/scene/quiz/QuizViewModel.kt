package eu.noharmdan.showcase.scene.quiz

import android.app.Application
import eu.noharmdan.common.base.BaseViewModel
import eu.noharmdan.data.datastore.AppDataStore
import eu.noharmdan.showcase.scene.quiz.util.toQuestionViewState
import eu.noharmdan.showcase.scene.quiz.util.withAnswerSetSelected
import eu.noharmdan.domain.usecase.GetRandomQuestionsUseCase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QuizViewModel(application: Application, private val appDataStore: AppDataStore) : BaseViewModel<QuizViewState, QuizViewEvent, QuizViewCommand>(application, QuizViewState()), KoinComponent {

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
                            questions = questions.map { question ->
                                question.toQuestionViewState()
                            }.toImmutableList(),
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

    private fun onAnswerSelected(answer: QuestionViewState.AnswerViewState) {
        with(currentState()) {
            defaultScope.launch {
                if (state !is QuizViewState.State.Questions || state.currentQuestion.answers.any { it.isSelected }) {
                    return@launch
                }

                if (answer.isCorrect) {
                    onCorrectAnswerSelected(state, answer)
                } else {
                    onWrongAnswerSelected(state, answer)
                }
            }
        }
    }

    private suspend fun QuizViewState.onCorrectAnswerSelected(
        state: QuizViewState.State.Questions,
        answer: QuestionViewState.AnswerViewState,
    ) {
        val currentQuestion = state.currentQuestion

        updateState {
            copy(
                state = state.copy(
                    questions = state.questions.withAnswerSetSelected(
                        currentQuestion = currentQuestion,
                        answer = answer,
                        isSelected = true
                    )
                )
            )
        }

        delay(timeMillis = NEXT_QUESTION_DELAY)

        val score = currentScore + 1
        val nextQuestionIndex = state.currentQuestionIndex + 1

        if (nextQuestionIndex == QUESTIONS_LIMIT) {
            updateState {
                copy(
                    currentScore = score,
                    state = state.copy(
                        questions = state.questions.withAnswerSetSelected(
                            currentQuestion = currentQuestion,
                            answer = answer,
                            isSelected = false
                        )
                    )
                )
            }

            getRandomQuestions(resetScore = false)
        } else {
            updateState {
                copy(
                    currentScore = score,
                    state = state.copy(
                        currentQuestionIndex = nextQuestionIndex
                    )
                )
            }
        }
    }

    private suspend fun QuizViewState.onWrongAnswerSelected(
        state: QuizViewState.State.Questions,
        answer: QuestionViewState.AnswerViewState
    ) {
        if (currentScore > highScore) {
            withContext(Dispatchers.IO) {
                appDataStore.setHighScore(highScore = currentScore)
            }
        }

        val currentQuestion = state.currentQuestion

        updateState {
            copy(
                state = state.copy(
                    questions = state.questions.withAnswerSetSelected(
                        currentQuestion = currentQuestion,
                        answer = answer,
                        isSelected = true
                    )
                )
            )
        }

        delay(timeMillis = NEXT_QUESTION_DELAY)

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
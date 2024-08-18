package eu.noharmdan.showcase.scene.quiz

import android.app.Application
import eu.noharmdan.common.base.BaseViewModel
import eu.noharmdan.data.datastore.QuizDataStore
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

/**
 * The main view model class for the [QuizActivity].
 *
 * Manages all of its data represented by the [QuizViewState], handles
 * the [QuizViewEvent]s (see [onEvent]) and publishes [QuizViewCommand]s.
 *
 * @param quizDataStore is passed as a parameter as it is used immediately
 * in `init`.
 *
 * *In a more "complete" application, the view model would typically
 * not access the data store or the use cases directly, but rather
 * through a repository which would limit visibility and shield code.
 * However, having a repository for the sake of having a repository
 * is not necessarily the best approach in small projects, where it
 * can add unnecessary obfuscation and boilerplate. On the other hand,
 * following the principles of clean architecture, especially when
 * multiple sources of data or business logic are used, or when only
 * a part of their capabilities is required, using repositories is
 * the perfect solution.*
 *
 */
class QuizViewModel(application: Application, private val quizDataStore: QuizDataStore) : BaseViewModel<QuizViewState, QuizViewEvent, QuizViewCommand>(application, QuizViewState()), KoinComponent {

    /**
     * The reusable use case is injected lazily as it may not be needed,
     * if the user chooses to close the application without starting a quiz.
     */
    private val getRandomQuestionsUseCase: GetRandomQuestionsUseCase by inject()

    init {
        // The latest high score is always kept updated in the view state
        quizDataStore.highScore.onEach { highScore ->
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

    /**
     * Executes the [getRandomQuestionsUseCase], updating the view state such
     * as that a loading is shown while new data is being fetched.
     *
     * If new questions are fetched successfully, they are published to the user
     * through updating the view state accordingly, otherwise the error state is
     * published.
     *
     * @param resetScore if `true`, the [QuizViewState.currentScore] is set to 0 -
     * typically when starting the quiz afresh, as opposed to loading more questions
     * to continue an already started quiz.
     */
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

    /**
     * To be called when the user selects an answer to the current question.
     *
     * Double-checks the correctness of the current [QuizViewState], e.g. that
     * a question is being shown to the user and no answer has already been
     * selected for it (which may happen e.g. if the user taps an answer multiple
     * times quickly).
     *
     * If the selected answer is the correct one, [onCorrectAnswerSelected] is
     * called, otherwise [onWrongAnswerSelected] is called.
     */
    private fun onAnswerSelected(answer: QuestionViewState.AnswerViewState) {
        with(currentState()) {
            defaultScope.launch {
                // Not showing a question at all - should never happen
                if (state !is QuizViewState.State.Questions) {
                    return@launch
                }

                val currentQuestion = state.currentQuestion

                // An index mismatch for the current question has occurred somehow
                if (currentQuestion == null) {
                    updateState {
                        copy(
                            state = QuizViewState.State.Error
                        )
                    }

                    return@launch
                }

                // An answer for this question was already selected
                if (currentQuestion.answers.any { it.isSelected }) {
                    return@launch
                }

                // All states are valid - check if the answer is correct
                if (answer.isCorrect) {
                    onCorrectAnswerSelected(state, currentQuestion, answer)
                } else {
                    onWrongAnswerSelected(state, currentQuestion, answer)
                }
            }
        }
    }

    /**
     * To be called if the [QuizViewState] is correct for an answer
     * to be selected, and the selected answer is the correct one.
     *
     * The view state is first updated to highlight the selected
     * answer and after [NEXT_QUESTION_DELAY] milliseconds, the user
     * is either shown the next question immediately, or new questions
     * are loaded through [getRandomQuestions] if needed.
     *
     * [state], [currentQuestion] and [answer] are passed as arguments
     * to ensure no synchronization or null-safety issues can arise
     * while the view state is evaluated and updated.
     */
    private suspend fun QuizViewState.onCorrectAnswerSelected(
        state: QuizViewState.State.Questions,
        currentQuestion: QuestionViewState,
        answer: QuestionViewState.AnswerViewState,
    ) {
        // Highlight the correct answer
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

        // Wait for a short while
        delay(timeMillis = NEXT_QUESTION_DELAY)

        // Evaluate the score and next question index
        val score = currentScore + 1
        val nextQuestionIndex = state.currentQuestionIndex + 1

        if (nextQuestionIndex == state.questions.size) {
            // Update the score and fetch new questions if no more available
            updateState {
                copy(
                    currentScore = score,
                )
            }

            getRandomQuestions(resetScore = false)
        } else {
            // Go to next question if available
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

    /**
     * To be called if the [QuizViewState] is correct for an answer
     * to be selected, and the selected answer is a wrong one.
     *
     * The view state is first updated to highlight the selected
     * answer and after [NEXT_QUESTION_DELAY] milliseconds, the user
     * is shown a screen informing them of their wrong answer,
     * with the option to try again.
     *
     * [state], [currentQuestion] and [answer] are passed as arguments
     * to ensure no synchronization or null-safety issues can arise
     * while the view state is evaluated and updated.
     */
    private suspend fun QuizViewState.onWrongAnswerSelected(
        state: QuizViewState.State.Questions,
        currentQuestion: QuestionViewState,
        answer: QuestionViewState.AnswerViewState
    ) {
        // If a new high score was reached, it is stored
        if (currentScore > highScore) {
            withContext(Dispatchers.IO) {
                quizDataStore.setHighScore(highScore = currentScore)
            }
        }

        // Highlight the wrong answer
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

        // Wait for a short while
        delay(timeMillis = NEXT_QUESTION_DELAY)

        // Show a "game over" screen
        updateState {
            copy(
                state = QuizViewState.State.WrongAnswer(question = currentQuestion)
            )
        }
    }

    companion object {
        /**
         * The number of questions to be loaded through [getRandomQuestions].
         *
         * *Kept at only 5 for demonstrative purposes.*
         */
        const val QUESTIONS_LIMIT = 5

        /**
         * The number of milliseconds for which an answer should stay
         * highlighted before moving on.
         */
        const val NEXT_QUESTION_DELAY = 800L
    }
}
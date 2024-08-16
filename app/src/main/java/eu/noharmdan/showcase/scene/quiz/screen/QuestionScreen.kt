package eu.noharmdan.showcase.scene.quiz.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.noharmdan.showcase.scene.quiz.OnEvent
import eu.noharmdan.showcase.scene.quiz.QuizViewEvent
import eu.noharmdan.showcase.scene.quiz.QuizViewState
import eu.noharmdan.showcase.scene.quiz.model.placeholderQuestion
import eu.noharmdan.showcase.ui.theme.ShowcaseTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun QuestionScreen(modifier: Modifier = Modifier, state: QuizViewState.State.Questions, onEvent: OnEvent) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxSize()
    ) {
        val currentQuestion = state.currentQuestion
        val currentAnswers = currentQuestion.answers

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentQuestion.text,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }

        val columns = 2
        val rows = (currentAnswers.size + 1) / columns

        for (rowIndex in 0 until rows) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                for (columnIndex in 0 until columns) {
                    val itemIndex = rowIndex * columns + columnIndex
                    if (itemIndex < currentAnswers.size) {
                        val answer = currentAnswers[itemIndex]

                        Button(
                            shape = RoundedCornerShape(size = 16.dp),
                            modifier = Modifier
                                .weight(weight = 1f)
                                .fillMaxHeight(),
                            onClick = {
                                onEvent(QuizViewEvent.OnAnswerSelected(answer = answer))
                            },
                            content = {
                                Text(
                                    text = answer.text,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(weight = 1f))
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuestionPreview() {
    ShowcaseTheme {
        QuestionScreen(
            state = QuizViewState.State.Questions(
                questions = persistentListOf(
                    placeholderQuestion
                ),
                currentQuestionIndex = 0
            ),
            onEvent = {}
        )
    }
}
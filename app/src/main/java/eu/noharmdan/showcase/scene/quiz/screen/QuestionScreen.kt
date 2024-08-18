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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.noharmdan.data.R
import eu.noharmdan.showcase.scene.quiz.OnEvent
import eu.noharmdan.showcase.scene.quiz.QuestionViewState
import eu.noharmdan.showcase.scene.quiz.QuizViewEvent
import eu.noharmdan.showcase.scene.quiz.model.placeholderQuestion
import eu.noharmdan.showcase.ui.theme.ShowcaseTheme
import eu.noharmdan.showcase.ui.theme.errorButtonColors
import eu.noharmdan.showcase.ui.theme.successButtonColors

@Composable
fun QuestionScreen(
    modifier: Modifier = Modifier,
    highScore: Int,
    currentScore: Int,
    currentQuestion: QuestionViewState,
    onEvent: OnEvent
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier = modifier
            .padding(all = 16.dp)
            .fillMaxSize()
    ) {
        val currentAnswers = currentQuestion.answers

        val scoreTextId = if (currentScore > highScore) R.string.new_high_score else R.string.current_score

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = currentQuestion.category.titleResId),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.End,
            )

            Text(
                text = stringResource(id = scoreTextId, currentScore),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.End,
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = currentQuestion.text,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
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

                        val buttonColors = when {
                            !answer.isSelected -> {
                                ButtonDefaults.buttonColors()
                            }

                            answer.isCorrect -> {
                                successButtonColors()
                            }

                            else -> {
                                errorButtonColors()
                            }
                        }

                        Button(
                            shape = RoundedCornerShape(size = 16.dp),
                            colors = buttonColors,
                            modifier = Modifier
                                .weight(weight = 1f)
                                .fillMaxHeight(),
                            onClick = {
                                onEvent(QuizViewEvent.OnAnswerSelected(answer = answer))
                            },
                            content = {
                                Text(
                                    text = answer.text,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                    style = MaterialTheme.typography.titleMedium,
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
            highScore = 17,
            currentScore = 6,
            currentQuestion = placeholderQuestion,
            onEvent = {}
        )
    }
}
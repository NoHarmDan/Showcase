package eu.noharmdan.showcase.scene.quiz.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.noharmdan.showcase.R
import eu.noharmdan.showcase.scene.quiz.OnEvent
import eu.noharmdan.showcase.scene.quiz.QuizViewEvent
import eu.noharmdan.showcase.scene.quiz.QuizViewState
import eu.noharmdan.showcase.scene.quiz.model.placeholderQuestion
import eu.noharmdan.showcase.ui.theme.ShowcaseTheme

@Composable
fun WrongAnswerScreen(modifier: Modifier = Modifier, state: QuizViewState.State.WrongAnswer, onEvent: OnEvent) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.wrong_answer),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )

        val question = state.question
        Text(
            text = stringResource(
                id = R.string.correct_answer_is,
                question.text,
                question.answers.first { it.isCorrect }.text
            ),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = {
                onEvent(QuizViewEvent.OnStartQuizSelected)
            },
            content = {
                Text(
                    text = stringResource(id = R.string.try_again),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WrongAnswerPreview() {
    ShowcaseTheme {
        WrongAnswerScreen(
            state = QuizViewState.State.WrongAnswer(
                question = placeholderQuestion
            ),
            onEvent = {}
        )
    }
}
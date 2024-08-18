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
import eu.noharmdan.data.R
import eu.noharmdan.showcase.scene.quiz.OnEvent
import eu.noharmdan.showcase.scene.quiz.QuizViewEvent
import eu.noharmdan.showcase.ui.theme.ShowcaseTheme

/**
 * The screen to be shown if an error occurs e.g. while fetching
 * quiz questions. Gives the user the option to try again,
 * triggering the [QuizViewEvent.OnTryAgainSelected] event.
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier, onEvent: OnEvent) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.something_went_wrong),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 56.dp, end = 56.dp, bottom = 32.dp)
        )

        Button(
            onClick = {
                onEvent(QuizViewEvent.OnTryAgainSelected)
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
private fun ErrorPreview() {
    ShowcaseTheme {
        ErrorScreen {}
    }
}
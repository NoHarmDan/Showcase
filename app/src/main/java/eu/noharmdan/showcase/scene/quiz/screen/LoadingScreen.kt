package eu.noharmdan.showcase.scene.quiz.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.noharmdan.showcase.ui.theme.ShowcaseTheme

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        /*
         * In a more real-life, user friendly scenario,
         * we would probably use shimmers rather than
         * circular progress bars, as well as possibly
         * inform the user what is loading as such.
         */
        CircularProgressIndicator(
            modifier = Modifier.size(size = 156.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingPreview() {
    ShowcaseTheme {
        LoadingScreen()
    }
}
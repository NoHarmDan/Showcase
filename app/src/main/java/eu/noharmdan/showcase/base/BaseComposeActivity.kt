package eu.noharmdan.showcase.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import eu.noharmdan.showcase.ui.theme.ShowcaseTheme

/**
 * A convenience superclass for all activities used in the app,
 * which can setup all common features and prepare the composable
 * context with the application theme, to be filled through
 * [RootCompose]
 *
 * *In a "real" single-activity application, this activity would naturally
 * become unnecessary. Or in a "real" multi-activity application, this
 * activity would usually declare much more than the simple code seen here.*
 */
abstract class BaseComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ShowcaseTheme {
                RootCompose()
            }
        }
    }

    @Composable
    abstract fun RootCompose()

}
package eu.noharmdan.showcase.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import eu.noharmdan.showcase.ui.theme.ShowcaseTheme

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
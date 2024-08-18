package eu.noharmdan.showcase.ui.theme

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun successButtonColors() = ButtonDefaults.buttonColors().copy(
    containerColor = GreenSuccess,
    contentColor = Color.White
)

@Composable
fun errorButtonColors() = ButtonDefaults.buttonColors().copy(
    containerColor = RedError,
    contentColor = Color.White
)
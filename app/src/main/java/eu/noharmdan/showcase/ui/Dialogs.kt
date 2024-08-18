package eu.noharmdan.showcase.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Simple alert dialog with two buttons to confirm or cancel an action.
 *
 * Is only shown if [showDialog] is `true`. This state is reset to `false`
 * automatically when the dialog is dismissed.
 */
@Composable
fun ConfirmationDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    text: String,
    confirmString: String,
    onConfirmClicked: (() -> Unit),
    cancelString: String,
    onCancelClicked: (() -> Unit)? = null,
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
                onCancelClicked?.invoke()
            },
            confirmButton = {
                DialogButton(
                    text = confirmString,
                ) {
                    showDialog.value = false
                    onConfirmClicked.invoke()
                }
            },
            dismissButton = {
                DialogButton(
                    text = cancelString,
                ) {
                    showDialog.value = false
                    onCancelClicked?.invoke()
                }
            },
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            text = {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )
    }
}

/**
 * Simple text button to be used in a dialog.
 */
@Composable
private fun DialogButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp),
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
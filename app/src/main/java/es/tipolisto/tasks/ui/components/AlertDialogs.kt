package es.tipolisto.tasks.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun MyAlertDialog( onDismiss:()->Unit, onConfirm:()->Unit){
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Cancel")
            }
        },
        title = { Text(text = "Warning ") },
        text = { Text(text = "Are you sure you want to delete the task?") },
    )
}
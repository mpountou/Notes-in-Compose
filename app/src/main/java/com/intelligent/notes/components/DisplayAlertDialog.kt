package com.intelligent.notes.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

/**
 * A simple custom alert dialog component
 */
@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    visibility: Boolean,
    closeDialog: () -> Unit,
    onYesClicked: () -> Unit,
) {
    if (visibility) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onYesClicked()
                        closeDialog()
                    })
                {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { closeDialog() })
                {
                    Text(text = "No")
                }
            },
            onDismissRequest = { closeDialog() }
        )
    }
}

@Preview
@Composable
private fun DialogPreview() {
    DisplayAlertDialog(title = "Test",
        message = "Test",
        visibility = true,
        closeDialog = {},
        onYesClicked = {}
    )
}

package com.pad.repitdoit.core.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun textPopup(
    title: String = "",
    placeHolderText: String = "",
    onConfirmAction: (String) -> Unit,
    onCancelAction: () -> Unit,
) {
    var input by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCancelAction,
        title = { Text(text = title) },
        text = {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text(text = placeHolderText) },
                singleLine = true
            )
        },
        dismissButton = {
            TextButton(onClick = onCancelAction) {
                Text("Cancelar")
            }
        },
        confirmButton = {
            TextButton(
                enabled = input.isNotBlank(),
                onClick = {
                    onConfirmAction(input)
                    onCancelAction()
                }
            ) {
                Text("Confirmar")
            }
        }
    )
}

package com.riakol.todojc.presentation.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.domain.model.Group

@Composable
fun RemoveTaskDialog(
    taskName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Remove task") },
        text = { Text("Are you sure you want to remove this task \"$taskName\"?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("No")
            }
        }
    )
}

@Composable
fun RenameCategoryDialog(
    category: Category,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by rememberSaveable { mutableStateOf(category.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename category") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("New name") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text) },
                enabled = text.isNotBlank() && text != category.name
            ) { Text("RENAME") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCEL") }
        }
    )
}

@Composable
fun RenameGroupDialog(
    group: Group,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by rememberSaveable { mutableStateOf(group.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename group") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("New name") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text) },
                enabled = text.isNotBlank() && text != group.name
            ) { Text("RENAME") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCEL") }
        }
    )
}

@Composable
fun RemoveGroupDialog(
    group: Group,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Remove group") },
        text = { Text("Are you sure you want to remove this group \"${group.name}\"?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("No")
            }
        }
    )
}

@Composable
fun RemoveCategoryDialog(
    category: Category,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Remove category") },
        text = { Text("Are you sure you want to remove this category \"${category.name}\"?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("No")
            }
        }
    )
}
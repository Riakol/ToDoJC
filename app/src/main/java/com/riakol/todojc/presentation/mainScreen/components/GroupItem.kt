package com.riakol.todojc.presentation.mainScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.riakol.todojc.domain.model.Group

@Composable
fun GroupItem(
    group: Group,
    onGroupClick: (Int) -> Unit = {}
) {
    Text(
        text = group.name,
        modifier = Modifier.clickable { onGroupClick(group.id) }
    )
}

@Composable
fun AddNewGroup(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var groupName by rememberSaveable { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {
            onDismiss
        },
        title = { Text("Enter group title") },
        text = {
            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(groupName)
                },
                enabled = groupName.isNotBlank()
            ) {
                Text("CREATE GROUP")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("CANCEL")
            }
        }
    )
}
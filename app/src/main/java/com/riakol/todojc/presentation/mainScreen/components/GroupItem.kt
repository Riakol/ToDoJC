package com.riakol.todojc.presentation.mainScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.riakol.todojc.domain.model.Group
import com.riakol.todojc.presentation.common.GroupOptionsMenu
import com.riakol.todojc.presentation.mainScreen.components.DynamicListEvent.OnDeleteGroupClick
import com.riakol.todojc.presentation.mainScreen.components.DynamicListEvent.OnRenameGroupClick

@Composable
fun GroupItem(
    group: Group,
    onEvent: (DynamicListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable { onEvent(DynamicListEvent.OnGroupClick(group.id)) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Folder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = group.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        GroupOptionsMenu(
            group = group,
            onEvent = { event ->
                when (event) {
                    is DynamicListEvent.OnRenameGroupClick -> {
                        onEvent(DynamicListEvent.OnRenameGroupClick(group))
                    }
                    is DynamicListEvent.OnDeleteGroupClick -> {
                        onEvent(DynamicListEvent.OnDeleteGroupClick(group))
                    }
                    else -> {}
                }
            }
        )
    }
}

// Остальное без изменений
@Composable
fun AddNewGroup(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var groupName by rememberSaveable { mutableStateOf("") }
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = { Text("Enter group title") },
        text = {
            androidx.compose.material3.OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group name") },
                singleLine = true
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = {
                    onConfirm(groupName)
                    onDismiss()
                },
                enabled = groupName.isNotBlank()
            ) {
                Text("CREATE GROUP")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(
                onClick = { onDismiss() }
            ) {
                Text("CANCEL")
            }
        }
    )
}
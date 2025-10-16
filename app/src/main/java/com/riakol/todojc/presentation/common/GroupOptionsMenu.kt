package com.riakol.todojc.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DriveFileMove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.riakol.todojc.domain.model.Group
import com.riakol.todojc.presentation.mainScreen.components.DynamicListEvent

@Composable
fun GroupOptionsMenu(
    group: Group,
    onEvent: (DynamicListEvent) -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { isMenuExpanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Group Options")
        }

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Rename group") },
                onClick = {
                    onEvent(DynamicListEvent.OnRenameGroupClick(group))
                    isMenuExpanded = false
                },
                leadingIcon = { Icon(Icons.Default.Update, contentDescription = "Rename") }
            )
            DropdownMenuItem(
                text = { Text("Move group") },
                onClick = {
//                    DynamicListEvent.OnMoveGroupClick(group.id)
                    isMenuExpanded = false
                },
                leadingIcon = { Icon(Icons.AutoMirrored.Filled.DriveFileMove, contentDescription = "Move") }
            )
            DropdownMenuItem(
                text = { Text("Delete group") },
                onClick = {
                    onEvent(DynamicListEvent.OnDeleteGroupClick(group))
                    isMenuExpanded = false
                },
                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = "Delete") }
            )
        }
    }
}
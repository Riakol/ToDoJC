package com.riakol.todojc.presentation.groupScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState


@Composable
fun GroupScreen(
//    groupId: Int,
    navController: NavController,
    viewModel: GroupScreenViewModel = hiltViewModel()
) {
    var dialogState by remember { mutableStateOf<DialogTaskState>(DialogTaskState.None) }
    val tasksState by viewModel.tasks.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    dialogState = DialogTaskState.AddNewTask
                }
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add task"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                IconButton(
                    onClick = { },
                ) {
                    Icon(
                        imageVector = Icons.Default.DensityMedium,
                        contentDescription = "More options",
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Group name: " + viewModel.groupDetails.collectAsState().value?.name ?: "")
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasksState) { task ->
                    Text(text = task.title)
                }
            }
        }

    }

    when (val currentDialog = dialogState) {
        is DialogTaskState.None -> {}
        is DialogTaskState.AddNewTask -> {
            AddNewTaskDialog(
                onDismiss = { dialogState = DialogTaskState.None },
                onConfirm = { taskName ->
                    viewModel.addTask(taskName)
                    dialogState = DialogTaskState.None
                }
            )
        }
        is DialogTaskState.RenameTask -> TODO()
    }

}

@Composable
fun AddNewTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var taskName by rememberSaveable { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {
            onDismiss
        },
        title = { Text("Enter task title") },
        text = {
            OutlinedTextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Task name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(taskName)
                },
                enabled = taskName.isNotBlank()
            ) {
                Text("CREATE TASK")
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

@Composable
fun TaskCardItem(taskTitle: String) {
    Row {
        Column {
            Card {
                Text(text=taskTitle)
            }
        }
    }
}
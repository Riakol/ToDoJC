package com.riakol.todojc.presentation.groupScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.riakol.todojc.domain.model.Task
import com.riakol.todojc.presentation.common.GroupOptionsMenu
import com.riakol.todojc.presentation.common.RemoveGroupDialog
import com.riakol.todojc.presentation.common.RemoveTaskDialog
import com.riakol.todojc.presentation.common.RenameGroupDialog
import com.riakol.todojc.presentation.mainScreen.components.DynamicListEvent
import com.riakol.todojs.R


@Composable
fun GroupScreen(
    navController: NavController,
    viewModel: GroupScreenViewModel = hiltViewModel()
) {
    var dialogState by remember { mutableStateOf<DialogTaskState>(DialogTaskState.None) }
    val tasksState by viewModel.tasks.collectAsStateWithLifecycle()
    val groupDetailsState by viewModel.groupDetails.collectAsStateWithLifecycle()
    val group = groupDetailsState
    var isInSelectionMode by remember { mutableStateOf(false) }
    val selectedTaskIds = remember { mutableStateSetOf<Int>() }

    BackHandler(enabled = isInSelectionMode) {
        isInSelectionMode = false
        selectedTaskIds.clear()
    }

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
            if (isInSelectionMode) {
                SelectionTopAppBar(
                    selectedItemCount = selectedTaskIds.size,
                    onCloseClick = {
                        isInSelectionMode = false
                        selectedTaskIds.clear()
                    },
                    onSelectAllClick = {
                        selectedTaskIds.addAll(tasksState.map { it.id })
                    },
                    onDeleteSelectedClick = {
                        dialogState = DialogTaskState.RemoveMultipleTasks(selectedTaskIds.toSet())
                    },
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    groupDetailsState?.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (group != null) {
                        GroupOptionsMenu(
                            group = group,
                            onEvent = { event ->
                                when (event) {
                                    is DynamicListEvent.OnRenameGroupClick -> {
                                        dialogState = DialogTaskState.RenameGroup(
                                            event.group.id
                                        )
                                    }

                                    is DynamicListEvent.OnDeleteGroupClick -> {
                                        dialogState = DialogTaskState.RemoveGroup(
                                            event.group
                                        )
                                    }

                                    else -> {}
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasksState) { task ->
                    val isSelected = task.id in selectedTaskIds
                    TaskCardItem(
                        task = task,
                        isSelected = isSelected,
                        onTaskClick = { taskId ->
                            if (isInSelectionMode) {
                                if (isSelected) {
                                    selectedTaskIds.remove(task.id)
                                } else {
                                    selectedTaskIds.add(task.id)
                                }
                                if (selectedTaskIds.isEmpty()) {
                                    isInSelectionMode = false
                                }
                            } else {
                                navController.navigate("task_screen/${taskId}")
                            }
                        },
                        onToggleClick = {
                            viewModel.toggleTaskCompletion(task)
                        },
                        onLongClick = {
                            isInSelectionMode = true
                            selectedTaskIds.add(task.id)
                            //dialogState = DialogTaskState.RemoveTask(task)
                        },
                        onFavouriteClick = {
                            viewModel.toggleFavoriteStatus(task)
                        }
                    )
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
        is DialogTaskState.RemoveTask -> {
            RemoveTaskDialog(
                taskName = currentDialog.task.title,
                onDismiss = { dialogState = DialogTaskState.None },
                onConfirm = {
                    viewModel.removeTask(currentDialog.task)
                    dialogState = DialogTaskState.None
                }
            )
        }

        is DialogTaskState.RemoveGroup -> {
            if (group != null) {
                RemoveGroupDialog(
                    group = group,
                    onDismiss = { dialogState = DialogTaskState.None },
                    onConfirm = {
                        viewModel.removeGroup(group)
                        dialogState = DialogTaskState.None
                    }
                )
            }
        }

        is DialogTaskState.RenameGroup -> {
            if (group != null) {
                RenameGroupDialog(
                    group = group,
                    onDismiss = { dialogState = DialogTaskState.None },
                    onConfirm = { newTitle ->
                        viewModel.onGroupNameChanged(group, newTitle)
                        dialogState = DialogTaskState.None
                    }
                )
            }
        }

        is DialogTaskState.RemoveMultipleTasks -> {
            RemoveTaskDialog(
                taskName = "${currentDialog.tasksId.size} items",
                onDismiss = { dialogState = DialogTaskState.None },
                onConfirm = {
                    viewModel.removeMultipleTasks(currentDialog.tasksId)
                    isInSelectionMode = false
                    selectedTaskIds.clear()
                    dialogState = DialogTaskState.None
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectionTopAppBar(
    selectedItemCount: Int,
    onCloseClick: () -> Unit,
    onSelectAllClick: () -> Unit,
    onDeleteSelectedClick: () -> Unit
) {
    TopAppBar(
        title = { Text("$selectedItemCount") },
        navigationIcon = {
            IconButton(
                onClick = onCloseClick
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close selection mode")
            }
        },
        actions = {
            var isMenuExpanded by remember { mutableStateOf(false) }

            Box {
                IconButton(onClick = { isMenuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More options")
                }

                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Select all") },
                        onClick = {
                            onSelectAllClick()
                            isMenuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Move") },
                        onClick = { /* TODO */ }
                    )
                    DropdownMenuItem(
                        text = { Text("Copy") },
                        onClick = { /* TODO */ }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete selected") },
                        onClick = {
                            onDeleteSelectedClick()
                            isMenuExpanded = false
                        }
                    )
                }
            }
        }
    )
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
fun TaskCardItem(
    task: Task,
    onTaskClick: (Int) -> Unit,
    isSelected: Boolean,
    onToggleClick: () -> Unit,
    onLongClick: () -> Unit,
    onFavouriteClick: () -> Unit
) {
    val completedCount = task.subTasks.count { it.isCompleted }
    val totalSubTasks = task.subTasks.size

    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onTaskClick(task.id)
                },
                onLongClick = {
                    onLongClick()
                }
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onToggleClick()
                }
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = "Task status",
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Circle,
                        contentDescription = "Task status",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column {
                Text(
                    text = "${task.title}\n ${if (totalSubTasks > 0) "$completedCount of $totalSubTasks" else ""}",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Start,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            if (task != null) {
                val text =
                    if (task.isFavourite) "Remove from favorites" else "Add to My Favourites"
                val icon =
                    if (task.isFavourite) R.drawable.heart else R.drawable.heart_outline

                IconButton(
                    onClick = {
                        onFavouriteClick()
                    }
                ) {
                    Icon(
                        painterResource(icon),
                        contentDescription = text,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
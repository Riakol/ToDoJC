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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.Surface
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(
    navController: NavController,
    viewModel: GroupScreenViewModel = hiltViewModel()
) {
    var dialogState by remember { mutableStateOf<DialogTaskState>(DialogTaskState.None) }
    val tasksState by viewModel.tasks.collectAsStateWithLifecycle()
    val groupDetailsState by viewModel.groupDetails.collectAsStateWithLifecycle()
    var isInSelectionMode by remember { mutableStateOf(false) }
    val selectedTaskIds = remember { mutableStateSetOf<Int>() }

    BackHandler(enabled = isInSelectionMode) {
        isInSelectionMode = false
        selectedTaskIds.clear()
    }

    Scaffold(
        topBar = {
            if (isInSelectionMode) {
                SelectionTopAppBar(
                    selectedItemCount = selectedTaskIds.size,
                    onCloseClick = {
                        isInSelectionMode = false
                        selectedTaskIds.clear()
                    },
                    onSelectAllClick = { selectedTaskIds.addAll(tasksState.map { it.id }) },
                    onDeleteSelectedClick = {
                        dialogState = DialogTaskState.RemoveMultipleTasks(selectedTaskIds.toSet())
                    }
                )
            } else {
                TopAppBar(
                    title = {
                        Text(groupDetailsState?.name ?: "Group")
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        groupDetailsState?.let { group ->
                            GroupOptionsMenu(
                                group = group,
                                onEvent = { event ->
                                    when (event) {
                                        is DynamicListEvent.OnRenameGroupClick -> dialogState = DialogTaskState.RenameGroup(group.id)
                                        is DynamicListEvent.OnDeleteGroupClick -> dialogState = DialogTaskState.RemoveGroup(group)
                                        else -> {}
                                    }
                                }
                            )
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (!isInSelectionMode) {
                FloatingActionButton(onClick = { dialogState = DialogTaskState.AddNewTask }) {
                    Icon(Icons.Default.Add, contentDescription = "Add task")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasksState) { task ->
                val isSelected = task.id in selectedTaskIds
                TaskCardItem(
                    task = task,
                    isSelected = isSelected,
                    onTaskClick = { taskId ->
                        if (isInSelectionMode) {
                            if (isSelected) selectedTaskIds.remove(task.id)
                            else selectedTaskIds.add(task.id)
                            if (selectedTaskIds.isEmpty()) isInSelectionMode = false
                        } else {
                            navController.navigate("task_screen/$taskId")
                        }
                    },
                    onToggleClick = { viewModel.toggleTaskCompletion(task) },
                    onLongClick = {
                        isInSelectionMode = true
                        selectedTaskIds.add(task.id)
                    },
                    onFavouriteClick = { viewModel.toggleFavoriteStatus(task) }
                )
            }
        }
    }

    // --- Dialogs ---
    when (val currentDialog = dialogState) {
        is DialogTaskState.None -> {}
        is DialogTaskState.AddNewTask -> AddNewTaskDialog(
            onDismiss = { dialogState = DialogTaskState.None },
            onConfirm = {
                viewModel.addTask(it)
                dialogState = DialogTaskState.None
            }
        )
        is DialogTaskState.RemoveTask -> RemoveTaskDialog(
            taskName = currentDialog.task.title,
            onDismiss = { dialogState = DialogTaskState.None },
            onConfirm = {
                viewModel.removeTask(currentDialog.task)
                dialogState = DialogTaskState.None
            }
        )
        is DialogTaskState.RemoveGroup -> groupDetailsState?.let { group ->
            RemoveGroupDialog(
                group = group,
                onDismiss = { dialogState = DialogTaskState.None },
                onConfirm = {
                    viewModel.removeGroup(group)
                    navController.popBackStack()
                }
            )
        }
        is DialogTaskState.RenameGroup -> groupDetailsState?.let { group ->
            RenameGroupDialog(
                group = group,
                onDismiss = { dialogState = DialogTaskState.None },
                onConfirm = {
                    viewModel.onGroupNameChanged(group, it)
                    dialogState = DialogTaskState.None
                }
            )
        }
        is DialogTaskState.RemoveMultipleTasks -> RemoveTaskDialog(
            taskName = "${currentDialog.tasksId.size} tasks",
            onDismiss = { dialogState = DialogTaskState.None },
            onConfirm = {
                viewModel.removeMultipleTasks(currentDialog.tasksId)
                selectedTaskIds.clear()
                isInSelectionMode = false
                dialogState = DialogTaskState.None
            }
        )
        else -> {}
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
        title = { Text("$selectedItemCount selected") },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        },
        actions = {
            var menuExpanded by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem({ Text("Select all") }, onClick = { onSelectAllClick(); menuExpanded = false })
                    DropdownMenuItem({ Text("Delete") }, onClick = { onDeleteSelectedClick(); menuExpanded = false })
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
        onDismissRequest = onDismiss,
        title = { Text("New task") },
        text = {
            OutlinedTextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Task title") }
            )
        },
        confirmButton = {
            TextButton(onClick = { if (taskName.isNotBlank()) onConfirm(taskName) }, enabled = taskName.isNotBlank()) {
                Text("CREATE")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("CANCEL") }
        }
    )
}

@Composable
fun TaskCardItem(
    task: Task,
    isSelected: Boolean,
    onTaskClick: (Int) -> Unit,
    onToggleClick: () -> Unit,
    onLongClick: () -> Unit,
    onFavouriteClick: () -> Unit
) {
    val completedCount = task.subTasks.count { it.isCompleted }
    val totalSubTasks = task.subTasks.size

    Surface(
        tonalElevation = if (isSelected) 2.dp else 1.dp,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onTaskClick(task.id) },
                onLongClick = onLongClick
            )
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            IconButton(onClick = onToggleClick) {
                Icon(
                    if (task.isCompleted) Icons.Outlined.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = "Toggle",
                    tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }

            Column(Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface
                )
                if (totalSubTasks > 0) {
                    Text(
                        text = "$completedCount of $totalSubTasks",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (task.isFavourite) {
                Icon(
                    painterResource(R.drawable.heart),
                    contentDescription = "Favourite",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
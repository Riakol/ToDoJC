package com.riakol.todojc.presentation.taskScreen

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.riakol.todojc.domain.model.SubTask
import com.riakol.todojc.presentation.common.RemoveTaskDialog
import com.riakol.todojc.presentation.taskScreen.utils.formatTimestamp
import com.riakol.todojs.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    navController: NavController,
    viewModel: TaskScreenViewModel = hiltViewModel()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val taskDetails = viewModel.taskDetails.collectAsStateWithLifecycle()
    val groupDetails by viewModel.groupDetails.collectAsStateWithLifecycle()
    val subTasks = viewModel.subTasks.collectAsStateWithLifecycle()
    val noteText by viewModel.noteText.collectAsStateWithLifecycle()

    val task = taskDetails.value

    var isAddStepEditing by remember { mutableStateOf(false) }
    var addStepText by remember { mutableStateOf("") }
    var localTaskTitle by remember { mutableStateOf("") }
    val addStepFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var expandedSubTaskId by remember { mutableStateOf<Int?>(null) }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(taskDetails) {
        taskDetails.value?.let {
            localTaskTitle = it.title
        }
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 20.dp, vertical = 8.dp),
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showBottomSheet = true
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = if (noteText.isEmpty()) {
                            "Add note"
                        } else {
                            "Tap to view"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp),
                        text = groupDetails?.name ?: ""
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = taskDetails.value?.let { task ->
                            formatTimestamp(task.creationDate)
                        } ?: "",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.toggleTaskCompletion(taskDetails.value!!) }
                    ) {
                        if (taskDetails.value?.isCompleted == true) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Task status",
                                modifier = Modifier.size(32.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.Circle,
                                contentDescription = "Task status",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    TextField(
                        value = localTaskTitle.ifEmpty { taskDetails.value?.title ?: "Loading.." },
                        onValueChange = {
                            localTaskTitle = it
                        },
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            textDecoration = if (taskDetails.value?.isCompleted == true) TextDecoration.LineThrough else TextDecoration.None),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .onFocusChanged(
                                onFocusChanged = { focusState ->
                                    if (!focusState.isFocused) {
                                        viewModel.onTaskNameChanged(localTaskTitle)
                                    }
                                }
                            ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            showDeleteDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteForever,
                            contentDescription = "Delete task"
                        )
                    }

                }
            }

            items(
                subTasks.value,
                key = { subTask -> subTask.id }
            ) { subTask ->
                val focusManager = LocalFocusManager.current
                SubTaskItem(
                    subTask = subTask,
                    viewModel = viewModel,
                    isExpanded = (subTask.id == expandedSubTaskId),
                    onExpandClick = {
                        expandedSubTaskId =
                            if (subTask.id == expandedSubTaskId) null else subTask.id
                    }
                )
            }

            item {
                if (isAddStepEditing) {
                    TextField(
                        value = addStepText,
                        onValueChange = { addStepText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp)
                            .focusRequester(addStepFocusRequester),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Circle,
                                contentDescription = "Input Icon"
                            )
                        },
                        placeholder = {
                            Text(text = "Add step")
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (addStepText.isNotBlank()) viewModel.addSubtask(
                                    addStepText
                                )
                                addStepText = ""
                                isAddStepEditing = false
                                focusManager.clearFocus()
                            }
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Gray
                        )
                    )
                    LaunchedEffect(Unit) {
                        addStepFocusRequester.requestFocus()
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { isAddStepEditing = true }
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add step"
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = "Add step"
                        )
                    }
                }
            }
            item {
                HorizontalDivider(
                    Modifier.padding(bottom = 20.dp, top = 10.dp),
                    DividerDefaults.Thickness,
                    DividerDefaults.color
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (task != null) {
                        val text = if (task.isFavourite) "Remove from favorites" else "Add to My Favourites"
                        val icon = if (task.isFavourite) Icons.Filled.Star else Icons.Outlined.Star

                        ActionCardItem(
                            text = text,
                            icon = icon,
                            onClick = {
                                taskDetails.value?.let {
                                    viewModel.toggleFavoriteStatus(task)
                                }
                            }
                        )
                    }
                    ActionCardItem(
                        text = "Remind me",
                        Icons.Default.AddAlert,
                        onClick = {}
                    )
                    ActionCardItem(
                        text = "Repeat",
                        Icons.Default.Repeat,
                        onClick = {}
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.saveNote()
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            )
            {
                Text("Add note", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = noteText,
                    onValueChange = { viewModel.onNoteTextChanged(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    placeholder = { Text("Write your note here...") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            }
        }
    }
    if (showDeleteDialog) {
        RemoveTaskDialog(
            onDismiss = {
                showDeleteDialog = false
            },
            onConfirm = {
                taskDetails.value?.let { task ->
                    viewModel.removeTask(task)
                    navController.navigateUp()
                }
                showDeleteDialog = false
            },
            taskName = taskDetails.value?.title ?: "Error title"
        )
    }
}


@Composable
fun ActionCardItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = text)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text)
        }
    }
}

@Composable
fun SubTaskItem(
    subTask: SubTask,
    viewModel: TaskScreenViewModel,
    isExpanded: Boolean,
    onExpandClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(subTask.title) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.clickable(
            onClick = onExpandClick
        )
    ) {
        LaunchedEffect(subTask.title) {
            if (text != subTask.title) {
                text = subTask.title
            }
        }

        TextField(
            value = text,
            onValueChange = { text = it },
            textStyle = TextStyle(
                textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        if (text.isBlank()) {
                            viewModel.removeSubTask(subTask)
                        } else if (text != subTask.title) {
                            viewModel.onSubTaskNameChanged(subTask, text)
                        }
                    }
                },
            keyboardActions = KeyboardActions(
                onSend = { focusManager.clearFocus() }
            ),
            leadingIcon = {
                IconButton(onClick = { viewModel.toggleSubTaskCompletion(subTask) }) {
                    if (subTask.isCompleted) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Mark as incomplete"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Circle,
                            contentDescription = "Mark as complete"
                        )
                    }
                }
            },
            trailingIcon = {
                Box {
                    IconButton(
                        onClick = { isExpanded = true }
                    ) {
                        Icon(
                            painterResource(R.drawable.more_vert_24px),
                            contentDescription = "options"
                        )
                    }
                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete step") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.DeleteForever,
                                    contentDescription = "Delete step"
                                )
                            },
                            onClick = {
                                viewModel.removeSubTask(subTask)
                                isExpanded = false
                            }
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        HorizontalDivider(
            modifier = Modifier.padding(start = 65.dp, end = 45.dp)
        )
    }
}

//@Composable
//fun DeleteConfirmationDialog(
//    onConfirm: () -> Unit,
//    onDismissRequest: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismissRequest,
//        title = { Text("Подтверждение") },
//        text = { Text("Вы точно хотите удалить эту задачу?") },
//        confirmButton = {
//            TextButton(
//                onClick = onConfirm
//            ) {
//                Text("Да")
//            }
//        },
//        dismissButton = {
//            TextButton(
//                onClick = onDismissRequest
//            ) {
//                Text("Нет")
//            }
//        }
//    )
//}
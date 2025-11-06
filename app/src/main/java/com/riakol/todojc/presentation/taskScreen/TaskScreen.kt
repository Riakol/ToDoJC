package com.riakol.todojc.presentation.taskScreen

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.riakol.todojc.domain.model.SubTask
import com.riakol.todojc.presentation.common.RemoveTaskDialog
import com.riakol.todojc.presentation.taskScreen.utils.formatTimestamp
import com.riakol.todojs.R
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    navController: NavController,
    viewModel: TaskScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val taskDetails by viewModel.taskDetails.collectAsStateWithLifecycle()
    val groupDetails by viewModel.groupDetails.collectAsStateWithLifecycle()
    val subTasks by viewModel.subTasks.collectAsStateWithLifecycle()
    val noteText by viewModel.noteText.collectAsStateWithLifecycle()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    var isAddStepEditing by remember { mutableStateOf(false) }
    var addStepText by remember { mutableStateOf("") }
    val addStepFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val selectedDate = remember { Calendar.getInstance() }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var localTaskTitle by remember { mutableStateOf("") }
    LaunchedEffect(taskDetails) {
        localTaskTitle = taskDetails?.title ?: ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(groupDetails?.name ?: "Task") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.DeleteForever, contentDescription = "Delete")
                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showBottomSheet = true },
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Text(
                        text = if (noteText.isEmpty()) "Add note" else "Tap to view note",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.toggleTaskCompletion(taskDetails!!) }) {
                        Icon(
                            if (taskDetails?.isCompleted == true) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                            contentDescription = "Status",
                            modifier = Modifier.size(28.dp),
                            tint = if (taskDetails?.isCompleted == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                    }
                    OutlinedTextField(
                        value = localTaskTitle.ifEmpty { taskDetails?.title ?: "" },
                        onValueChange = { localTaskTitle = it },
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                            textDecoration = if (taskDetails?.isCompleted == true) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    viewModel.onTaskNameChanged(localTaskTitle)
                                }
                            },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = taskDetails?.let { formatTimestamp(it.creationDate) } ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(subTasks, key = { it.id }) { subTask ->
                SubTaskItem(subTask = subTask, viewModel = viewModel)
            }

            item {
                if (isAddStepEditing) {
                    OutlinedTextField(
                        value = addStepText,
                        onValueChange = { addStepText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(addStepFocusRequester),
                        placeholder = { Text("Add step") },
                        leadingIcon = {
                            Icon(Icons.Outlined.Circle, contentDescription = null)
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (addStepText.isNotBlank()) {
                                    viewModel.addSubtask(addStepText)
                                    addStepText = ""
                                }
                                isAddStepEditing = false
                                focusManager.clearFocus()
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    LaunchedEffect(Unit) { addStepFocusRequester.requestFocus() }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { isAddStepEditing = true },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add step")
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Add step", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            item {
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ActionCardItem(
                        text = if (taskDetails?.isFavourite == true) "Remove from favourites" else "Add to favourites",
                        icon = if (taskDetails?.isFavourite == true) Icons.Filled.Star else Icons.Outlined.Star,
                        onClick = { taskDetails?.let { viewModel.toggleFavoriteStatus(it) } }
                    )
                    ActionCardItem(text = "Remind me", icon = Icons.Default.AddAlert) {
                        showDatePicker = true
                    }
                    ActionCardItem(text = "Repeat", icon = Icons.Default.Repeat) {}
                }
            }
        }
    }

    // --- Bottom sheet for note ---
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
            ) {
                Text("Note", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { viewModel.onNoteTextChanged(it) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 8,
                    placeholder = { Text("Write your note...") },
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) showBottomSheet = false
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            }
        }
    }

    // --- Delete dialog ---
    if (showDeleteDialog) {
        RemoveTaskDialog(
            taskName = taskDetails?.title ?: "Task",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                taskDetails?.let {
                    viewModel.removeTask(it)
                    navController.navigateUp()
                }
                showDeleteDialog = false
            }
        )
    }

    // --- Date/Time pickers + Notification permission --
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) //viewModel.setReminder(selectedDate.timeInMillis)
        else Toast.makeText(context, "Notifications disabled", Toast.LENGTH_SHORT).show()
    }

    if (showDatePicker) {
        val now = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, y, m, d ->
                selectedDate.set(y, m, d)
                showDatePicker = false
                showTimePicker = true
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    if (showTimePicker) {
        val now = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, h, m ->
                selectedDate.set(Calendar.HOUR_OF_DAY, h)
                selectedDate.set(Calendar.MINUTE, m)
                val hasPermission = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
                if (hasPermission) //viewModel.setReminder(selectedDate.timeInMillis)
                else notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                showTimePicker = false
            },
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            true
        ).show()
    }
}

@Composable
fun ActionCardItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun SubTaskItem(subTask: SubTask, viewModel: TaskScreenViewModel) {
    var text by remember { mutableStateOf(subTask.title) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(subTask.title) {
        if (text != subTask.title) text = subTask.title
    }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        textStyle = TextStyle(
            textDecoration = if (subTask.isCompleted) TextDecoration.LineThrough else TextDecoration.None
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    if (text.isBlank()) viewModel.removeSubTask(subTask)
                    else if (text != subTask.title) viewModel.onSubTaskNameChanged(subTask, text)
                }
            },
        leadingIcon = {
            IconButton(onClick = { viewModel.toggleSubTaskCompletion(subTask) }) {
                Icon(
                    if (subTask.isCompleted) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = "Toggle",
                    tint = if (subTask.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { viewModel.removeSubTask(subTask) }) {
                Icon(Icons.Default.DeleteForever, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        },
        shape = RoundedCornerShape(12.dp)
    )
}
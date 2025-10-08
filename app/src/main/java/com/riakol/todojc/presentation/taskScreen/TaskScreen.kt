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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.riakol.todojs.R

@Composable
fun TaskScreen(
    navController: NavController,
    viewModel: TaskScreenViewModel = hiltViewModel()
) {
    val taskDetails = viewModel.taskDetails.collectAsStateWithLifecycle()
    val subTasks = viewModel.subTasks.collectAsStateWithLifecycle()
    var isAddStepEditing by remember { mutableStateOf(false) }
    var addStepText by remember { mutableStateOf("") }
    val addStepFocusRequester = remember { FocusRequester() }
    var noteText = rememberTextFieldState()
    val focusManager = LocalFocusManager.current

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
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
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Here name of group"
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.circle_outline),
                        contentDescription = "Task status",
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = taskDetails.value?.title ?: "Загрузка...",
                    style = TextStyle(fontSize = 24.sp)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(subTasks.value) { subTask ->
                    var isExpanded by remember { mutableStateOf(false) }

                    Column {
                        TextField(
                            value = subTask.title,
                            onValueChange = { newText ->
                                viewModel.onSubTaskChanged(subTask, newText)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Circle,
                                    contentDescription = "Input Icon"
                                )
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
                                    viewModel.addSubtask(addStepText)
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
                        HorizontalDivider(
                            Modifier.padding(bottom = 20.dp, top = 10.dp),
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ActionCardItem(
                            text = "Add to My Favourites",
                            Icons.Default.WbSunny,
                            onClick = {}
                        )
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
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 20.dp)
                        ) {
                            TextField(
                                state = noteText,
                                lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 2),
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text(text = "Add note") },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        }
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
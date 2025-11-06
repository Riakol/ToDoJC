package com.riakol.todojc.presentation.mainScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.riakol.todojc.presentation.common.RemoveCategoryDialog
import com.riakol.todojc.presentation.common.RemoveGroupDialog
import com.riakol.todojc.presentation.common.RenameCategoryDialog
import com.riakol.todojc.presentation.common.RenameGroupDialog
import com.riakol.todojc.presentation.mainScreen.DialogMainScreenState.AddNewGroup
import com.riakol.todojc.presentation.mainScreen.DialogMainScreenState.RemoveCategory
import com.riakol.todojc.presentation.mainScreen.DialogMainScreenState.RemoveGroup
import com.riakol.todojc.presentation.mainScreen.DialogMainScreenState.RenameCategory
import com.riakol.todojc.presentation.mainScreen.DialogMainScreenState.RenameGroup
import com.riakol.todojc.presentation.mainScreen.components.AddNewCategoryDialog
import com.riakol.todojc.presentation.mainScreen.components.AddNewGroup
import com.riakol.todojc.presentation.mainScreen.components.CategoryItemDropdownMenu
import com.riakol.todojc.presentation.mainScreen.components.DynamicListEvent
import com.riakol.todojc.presentation.mainScreen.components.GroupItem
import com.riakol.todojs.R

@Composable
fun Main_screen(
    navController: NavController,
    viewModel: MainViewModel
) {
    var dialogState by remember { mutableStateOf<DialogMainScreenState>(DialogMainScreenState.None) }
    val itemsState by viewModel.mainScreenItems.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            MainBottomAppBar(
                onNewListClick = { dialogState = DialogMainScreenState.AddNewCategory },
                onNewGroupClick = { dialogState = DialogMainScreenState.AddNewUnassignedGroup }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                StaticActionList()
            }
            items(
                itemsState,
                contentType = { item -> item.javaClass }
            ) { item ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    when (item) {
                        is MainScreenItem.CategoryItem -> {
                            CategoryItemDropdownMenu(
                                category = item.category,
                                onEvent = { event ->
                                    when (event) {
                                        is DynamicListEvent.OnRenameCategoryClick -> dialogState = RenameCategory(event.category)
                                        is DynamicListEvent.OnDeleteCategoryClick -> dialogState = RemoveCategory(event.category)
                                        else -> { }
                                    }
                                },
                            )
                        }
                        is MainScreenItem.GroupItem -> {
                            GroupItem(
                                item.group,
                                onEvent = { event ->
                                    when (event) {
                                        is DynamicListEvent.OnGroupClick -> navController.navigate("group_screen/${event.groupId}")
                                        is DynamicListEvent.OnRenameGroupClick -> dialogState = RenameGroup(event.group)
                                        is DynamicListEvent.OnDeleteGroupClick -> dialogState = RemoveGroup(event.group)
                                        is DynamicListEvent.OnAddNewGroupInListClick -> dialogState = AddNewGroup(event.categoryId)
                                        else -> {}
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    HandleDialogs(
        dialogState = dialogState,
        viewModel = viewModel,
        onDismiss = { dialogState = DialogMainScreenState.None }
    )
}

@Composable
private fun MainBottomAppBar(
    onNewListClick: () -> Unit,
    onNewGroupClick: () -> Unit
) {
    BottomAppBar(
        containerColor = Color.Transparent,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clickable(onClick = onNewListClick),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(id = R.drawable.plus), contentDescription = "New list")
                Spacer(modifier = Modifier.width(8.dp))
                Text("New list", style = MaterialTheme.typography.labelLarge)
            }
            IconButton(onClick = onNewGroupClick) {
                Icon(painter = painterResource(id = R.drawable.card_plus_outline), contentDescription = "New group")
            }
        }
    }
}

@Composable
private fun StaticActionList() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        QuickActionItem(icon = Icons.Default.FavoriteBorder, label = "My Favourites")
        QuickActionItem(icon = painterResource(R.drawable.calendar_range), label = "Planned")
        QuickActionItem(icon = painterResource(R.drawable.account), label = "Assigned to me")
        QuickActionItem(icon = painterResource(R.drawable.home_plus_outline), label = "Tasks")
        Spacer(modifier = Modifier.height(16.dp))
        Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun QuickActionItem(icon: Any, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* TODO */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (icon) {
            is androidx.compose.ui.graphics.painter.Painter -> Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            is androidx.compose.ui.graphics.vector.ImageVector -> Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun HandleDialogs(
    dialogState: DialogMainScreenState,
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    when (val currentDialog = dialogState) {
        is DialogMainScreenState.None -> {}
        is DialogMainScreenState.AddNewCategory -> {
            AddNewCategoryDialog(
                onDismiss = { onDismiss() },
                onConfirm = { newName ->
                    viewModel.addCategory(newName)
                    onDismiss()
                }
            )
        }

        is DialogMainScreenState.RenameCategory -> {
            RenameCategoryDialog(
                category = currentDialog.category,
                onDismiss = { onDismiss() }
            ) { newTitle ->
                viewModel.renameCategory(currentDialog.category, newTitle)
                onDismiss()
            }
        }

        is DialogMainScreenState.AddNewUnassignedGroup -> {
            AddNewGroup(
                onDismiss = onDismiss,
                onConfirm = { groupName ->
                    viewModel.addUnassignedGroup(groupName)
                    onDismiss
                }
            )
        }

        is DialogMainScreenState.AddNewGroup -> {
            AddNewGroup(
                onDismiss = onDismiss,
                onConfirm = { groupName ->
                    viewModel.addGroup(groupName, currentDialog.categoryId)
                    onDismiss()
                }
            )
        }

        is DialogMainScreenState.RenameGroup -> {
            RenameGroupDialog(
                group = currentDialog.group,
                onDismiss = { onDismiss() },
                onConfirm = { newTitle ->
                    viewModel.onGroupNameChanged(currentDialog.group, newTitle)
                    onDismiss()
                }
            )
        }
        is DialogMainScreenState.RemoveGroup -> {
            RemoveGroupDialog(
                group = currentDialog.group,
                onDismiss = { onDismiss() },
                onConfirm = {
                    viewModel.removeGroup(currentDialog.group)
                    onDismiss()
                }
            )
        }
        is DialogMainScreenState.MoveGroup -> {}
        is DialogMainScreenState.RemoveCategory -> {
            RemoveCategoryDialog(
                category = currentDialog.category,
                onDismiss = { onDismiss() },
                onConfirm = {
                    viewModel.removeCategory(currentDialog.category)
                    onDismiss()
                }
            )
        }
    }
}
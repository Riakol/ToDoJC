import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.ColumnInfo
import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.domain.model.Group
import com.riakol.todojc.presentation.MainViewModel
import com.riakol.todojc.presentation.mainScreen.DialogState
import com.riakol.todojc.presentation.mainScreen.MainScreenItem
import com.riakol.todojs.R

@Composable
fun Main_screen(
    viewModel: MainViewModel
) {
    var dialogState by remember { mutableStateOf<DialogState>(DialogState.None) }
    val itemsState by viewModel.mainScreenItems.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Transparent,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            dialogState = DialogState.AddNewCategory
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus), contentDescription = ""
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("New list")
                    }
                    IconButton(
                        onClick = {
                            dialogState = DialogState.AddNewUnassignedGroup
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.card_plus_outline),
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 64.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.clickable {}, verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.white_balance_sunny),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("My Day")
                }
                Row(
                    modifier = Modifier.clickable {}, verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_range),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Planned")
                }
                Row(
                    modifier = Modifier.clickable {}, verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.account), contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Assigned to me")
                }
                Row(
                    modifier = Modifier.clickable {}, verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.home_plus_outline),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Tasks")
                }
                HorizontalDivider(thickness = 2.dp)
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    itemsState,
                    contentType = { item -> item.javaClass }
                ) { item ->
                    when (item) {
                        is MainScreenItem.CategoryItem -> {
                            CategoryItemDropdownMenu(
                                item.category,
                                onAddNewGroupClick = {
                                    dialogState = DialogState.AddNewGroup(item.category.id)
                                }
                            )
                        }

                        is MainScreenItem.GroupItem -> {
                            GroupItem(item.group)
                        }
                    }
                }

            }
        }

    }

    when (val currentDialog = dialogState) {
        is DialogState.None -> {}
        is DialogState.AddNewCategory -> {
            AddNewCategoryDialog(
                onDismiss = { dialogState = DialogState.None },
                onConfirm = { newName ->
                    viewModel.addCategory(newName)
                    dialogState = DialogState.None
                }
            )
        }

        is DialogState.RenameCategory -> TODO()
        is DialogState.AddNewUnassignedGroup -> {
            AddNewGroup(
                onDismiss = { dialogState = DialogState.None },
                onConfirm = { groupName ->
                    viewModel.addUnassignedGroup(groupName)
                    dialogState = DialogState.None
                }
            )
        }

        is DialogState.AddNewGroup -> {
            AddNewGroup(
                onDismiss = { dialogState = DialogState.None },
                onConfirm = { groupName ->
                    viewModel.addGroup(groupName, currentDialog.categoryId)
                    dialogState = DialogState.None
                }
            )
        }
    }
}

@Composable
fun CategoryItemDropdownMenu(
    category: Category,
    onAddNewGroupClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 0f else 90f,
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isExpanded) {
                Icon(
                    painter = painterResource(id = R.drawable.card),
                    contentDescription = "category icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                category.name,
                modifier = Modifier.weight(1f)
            )
            if (isExpanded) CategoryItemOptions(onAddGroupClick = onAddNewGroupClick)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Expand",
                modifier = Modifier.rotate(rotationAngle)
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 32.dp, top = 8.dp, bottom = 8.dp)
                    .height(IntrinsicSize.Min),
            ) {
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    thickness = 3.dp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                ) {
                    category.groups.forEach {
                        GroupItem(it)
                    }
                }
            }
        }
    }
}

@Composable
fun GroupItem(
    group: Group
) {
    Text(
        text = group.name,
        modifier = Modifier.padding(vertical = 8.dp))
}


@Composable
fun CategoryItemOptions(onAddGroupClick: () -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { isExpanded = !isExpanded },
        ) {
            Icon(
                imageVector = Icons.Default.DensityMedium,
                contentDescription = "More options",
            )
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("Rename group") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Update,
                        contentDescription = "Rename group"
                    )
                },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Add group") },
                leadingIcon = {
                    Icon(
                        Icons.Default.AddCircleOutline,
                        contentDescription = "Add group"
                    )
                },
                onClick = {
                    onAddGroupClick()
                    isExpanded = false
                }
            )
        }
    }
}

@Composable
fun AddNewCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var listName by rememberSaveable { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = {
            onDismiss
        },
        title = { Text("Enter list title") },
        text = {
            OutlinedTextField(
                value = listName,
                onValueChange = { listName = it },
                label = { Text("List name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(listName)
                },
                enabled = listName.isNotBlank()
            ) {
                Text("CREATE LIST")
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
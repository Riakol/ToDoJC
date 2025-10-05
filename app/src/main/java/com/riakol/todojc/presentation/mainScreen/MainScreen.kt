package com.riakol.todojc.presentation.mainScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.riakol.todojc.presentation.mainScreen.components.AddNewCategoryDialog
import com.riakol.todojc.presentation.mainScreen.components.AddNewGroup
import com.riakol.todojc.presentation.mainScreen.components.CategoryItemDropdownMenu
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
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
            StaticActionList()
            DynamicContentList(
                itemsState,
                navController,
                onGroupClick = { groupId ->
                    navController.navigate("task_screen/$groupId")
                },
                onAddNewGroupInListClick = { categoryId ->
                    dialogState = DialogMainScreenState.AddNewGroup(categoryId)
                }
            )

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
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clickable(
                    onClick = onNewListClick
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus), contentDescription = ""
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("New list")
            }
            IconButton(
                onClick = onNewGroupClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.card_plus_outline),
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun StaticActionList() {
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
}

@Composable
private fun DynamicContentList(
    itemsState: List<MainScreenItem>,
    navController: NavController,
    onGroupClick: (Int) -> Unit,
    onAddNewGroupInListClick: (Int) -> Unit
) {
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
                        onGroupClick = onGroupClick,
                        onAddNewGroupClick = {
                            onAddNewGroupInListClick(item.category.id)
                        }
                    )
                }

                is MainScreenItem.GroupItem -> {
                    GroupItem(
                        item.group,
                        onGroupClick = onGroupClick
                    )
                }
            }
        }
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
                onDismiss = onDismiss,
                onConfirm = { newName ->
                    viewModel.addCategory(newName)
                    onDismiss
                }
            )
        }

        is DialogMainScreenState.RenameCategory -> TODO()
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
                    onDismiss
                }
            )
        }
    }
}
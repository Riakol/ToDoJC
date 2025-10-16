package com.riakol.todojc.presentation.mainScreen.components

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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.riakol.todojc.domain.model.Category

@Composable
fun CategoryItemOptions(
    category: Category,
    onEvent: (DynamicListEvent) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { isExpanded = !isExpanded },
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Group Options",
            )
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("Rename category") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Update,
                        contentDescription = "Rename category"
                    )
                },
                onClick = {
                    onEvent(DynamicListEvent.OnRenameCategoryClick(category))
                    isExpanded = false
                }
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
                    onEvent(DynamicListEvent.OnAddNewGroupInListClick(category.id))
                    isExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Delete category") },
                leadingIcon = {
                    Icon(
                        Icons.Default.DeleteForever,
                        contentDescription = "delete category"
                    )
                },
                onClick = {
                    onEvent(DynamicListEvent.OnDeleteCategoryClick(category))
                    isExpanded = false
                }
            )
        }
    }
}

    @Composable
    fun CategoryItemDropdownMenu(
        category: Category,
        onEvent: (DynamicListEvent) -> Unit
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
                    .defaultMinSize(minHeight = 48.dp)
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isExpanded) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = "category icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    category.name,
                    modifier = Modifier.weight(1f)
                )
                if (isExpanded) CategoryItemOptions(
                    category = category,
                    onEvent = onEvent
                )
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
                            .defaultMinSize(minHeight = 48.dp)
                            .padding(start = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        category.groups.forEach { group ->
                            GroupItem(
                                group = group,
                                onEvent = onEvent
                            )
                        }
                    }
                }
            }
        }
    }
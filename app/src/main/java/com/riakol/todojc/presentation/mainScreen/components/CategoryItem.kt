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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DensityMedium
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.riakol.todojc.domain.model.Category
import com.riakol.todojs.R

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
fun CategoryItemDropdownMenu(
    category: Category,
    onAddNewGroupClick: () -> Unit,
    onGroupClick: (Int) -> Unit
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
                        .padding(start = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    category.groups.forEach {
                        GroupItem(
                            it
                        ) { groupId ->
                            onGroupClick(groupId)
                        }
                    }
                }
            }
        }
    }
}
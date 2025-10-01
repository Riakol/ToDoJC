package com.riakol.todojc.presentation.groupScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController


@Composable
fun GroupScreen(
//    groupId: Int,
    navController: NavController,
    viewModel: GroupScreenViewModel = hiltViewModel()
) {
    val tasksState by viewModel.tasks.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                IconButton(
                    onClick = { },
                ) {
                    Icon(
                        imageVector = Icons.Default.DensityMedium,
                        contentDescription = "More options",
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
//            Text(text = "Group ID: $groupId")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Здесь будет информация о группе")
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(tasksState) { task ->
                    Text(text = task.title)
                }
            }
        }

    }

}
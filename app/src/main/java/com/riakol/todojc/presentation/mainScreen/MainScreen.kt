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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.presentation.MainViewModel
import com.riakol.todojs.R

@Composable
fun Main_screen(
    viewModel: MainViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    val categoriesState by viewModel.categories.collectAsStateWithLifecycle()

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
                            showDialog = true
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
                        onClick = {},
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
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(categoriesState) { category ->
                    CategoryItem(category)
                }
            }
        }

    }
    if (showDialog) {
        var listName by rememberSaveable { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                showDialog = false
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
                        if (listName.isNotBlank()) {
                            viewModel.addCategory(listName)
                        }
                        showDialog = false
                    },
                    enabled = listName.isNotBlank()
                ) {
                    Text("CREATE LIST")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("CANCEL")
                }
            }
        )
    }
}

@Composable
fun CategoryItem(category: Category) {
    Text(category.name)
}
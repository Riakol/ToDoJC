package com.riakol.todojc

import Main_screen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.riakol.todojc.data.local.dao.CategoryListDao
import com.riakol.todojc.data.local.dao.GroupListDao
import com.riakol.todojc.presentation.MainViewModel
import com.riakol.todojs.ui.theme.TodoJSTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var categoryDao: CategoryListDao
    @Inject lateinit var groupDao: GroupListDao
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoJSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Main_screen(viewModel)
                }
            }
        }
    }
}

package com.riakol.todojc

import Main_screen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.riakol.todojc.data.local.dao.CategoryListDao
import com.riakol.todojc.data.local.dao.GroupListDao
import com.riakol.todojc.presentation.groupScreen.GroupScreen
import com.riakol.todojc.presentation.mainScreen.MainViewModel
import com.riakol.todojc.presentation.taskScreen.TaskScreen
import com.riakol.todojs.ui.theme.TodoJSTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoJSTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = "main_screen",
                        enterTransition = {
                            slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth })
                        },
                        exitTransition = {
                            slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth })
                        },
                        popEnterTransition = {
                            slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth })
                        },
                        popExitTransition = {
                            slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
                        }
                    ) {
                        composable("main_screen") {
                            Main_screen(
                                navController,
                                viewModel
                            )
                        }
                        composable(
                            "group_screen/{groupId}",
                            arguments = listOf(navArgument("groupId") { type = NavType.IntType })
                        ) { navBackStackEntry ->
                            val groupId = navBackStackEntry.arguments?.getInt("groupId")
                            if (groupId != null) {
                                GroupScreen(
                                    navController = navController
                                )
                            }

                        }
                        composable(
                            "task_screen/{taskId}",
                            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
                        ) {
                            val taskId = it.arguments?.getInt("taskId")
                            if (taskId != null) {
                                TaskScreen(
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.example.module_3_tasks_7_8.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.module_3_tasks_7_8.presentation.ui.screen.TodoDetailScreen
import com.example.module_3_tasks_7_8.presentation.ui.screen.TodoListScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "todo_list"
    ) {
        composable("todo_list") {
            TodoListScreen(
                onItemClick = { todoId ->
                    navController.navigate("todo_detail/$todoId")
                }
            )
        }

        composable(
            "todo_detail/{todoId}",
            arguments = listOf(navArgument("todoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getInt("todoId") ?: 0
            TodoDetailScreen(
                todoId = todoId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
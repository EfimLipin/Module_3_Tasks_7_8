package com.example.module_3_tasks_7_8.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.module_3_tasks_7_8.presentation.viewmodel.TodoViewModel

@Composable
fun TodoDetailScreen(
    todoId: Int,
    onBackClick: () -> Unit
) {
    val viewModel: TodoViewModel = viewModel()
    val todos by viewModel.todos.collectAsState()
    val todo = todos.find { it.id == todoId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBackClick,
            modifier = androidx.compose.ui.Modifier.testTag("back_button")) {
            Text("Назад")
        }

        if (todo != null) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    text = todo.title,
                    style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = todo.description,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Статус: ${if (todo.isCompleted) "Выполнена" else "Не выполнена"}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            Text("Задача не найдена")
        }
    }
}
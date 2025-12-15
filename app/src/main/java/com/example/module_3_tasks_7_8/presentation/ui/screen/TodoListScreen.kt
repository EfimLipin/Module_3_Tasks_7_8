package com.example.module_3_tasks_7_8.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.module_3_tasks_7_8.presentation.ui.component.TodoItemCard
import com.example.module_3_tasks_7_8.presentation.viewmodel.TodoViewModel

@Composable
fun TodoListScreen(
    onItemClick: (Int) -> Unit
) {
    val viewModel: TodoViewModel = viewModel()
    val todos by viewModel.todos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Список задач (${todos.size})",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyColumn {
            items(todos) { todo ->
                TodoItemCard(
                    todo = todo,
                    onCheckedChange = { viewModel.toggleTodo(todo.id) },
                    onClick = { onItemClick(todo.id) }
                )
            }
        }
    }
}
package com.example.module_3_tasks_7_8.domain.repository

import com.example.module_3_tasks_7_8.domain.model.TodoItem
import kotlinx.coroutines.flow.StateFlow

interface TodoRepository {
    val todos: StateFlow<List<TodoItem>>
    suspend fun getTodos(): List<TodoItem>
    suspend fun toggleTodo(id: Int)
}
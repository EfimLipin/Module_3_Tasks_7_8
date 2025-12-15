package com.example.module_3_tasks_7_8.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.module_3_tasks_7_8.domain.model.TodoItem
import com.example.module_3_tasks_7_8.domain.usecase.GetTodosUseCase
import com.example.module_3_tasks_7_8.domain.usecase.ToggleTodoUseCase
import com.example.module_3_tasks_7_8.domain.repository.TodoRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel(
    private val getTodosUseCase: GetTodosUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase,
    repository: TodoRepository
) : ViewModel() {

    val todos: StateFlow<List<TodoItem>> = repository.todos

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            getTodosUseCase()
        }
    }

    fun toggleTodo(id: Int) {
        viewModelScope.launch {
            toggleTodoUseCase(id)
        }
    }
}
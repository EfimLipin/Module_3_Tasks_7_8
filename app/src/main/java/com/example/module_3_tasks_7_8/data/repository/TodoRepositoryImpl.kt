package com.example.module_3_tasks_7_8.data.repository

import com.example.module_3_tasks_7_8.data.local.TodoJsonDataSource
import com.example.module_3_tasks_7_8.domain.model.TodoItem
import com.example.module_3_tasks_7_8.domain.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TodoRepositoryImpl(
    private val dataSource: TodoJsonDataSource
) : TodoRepository {

    private val _todos = MutableStateFlow<List<TodoItem>>(
        dataSource.getTodos().map { dto ->
            TodoItem(
                id = dto.id,
                title = dto.title,
                description = dto.description,
                isCompleted = dto.isCompleted
            )
        }
    )

    override val todos: StateFlow<List<TodoItem>> = _todos.asStateFlow()

    override suspend fun getTodos(): List<TodoItem> = _todos.value

    override suspend fun toggleTodo(id: Int) {
        _todos.update { currentTodos ->
            currentTodos.map { todo ->
                if (todo.id == id) {
                    todo.copy(isCompleted = !todo.isCompleted)
                } else {
                    todo
                }
            }
        }
    }
}
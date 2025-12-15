package com.example.module_3_tasks_7_8.domain.usecase

import com.example.module_3_tasks_7_8.domain.model.TodoItem
import com.example.module_3_tasks_7_8.domain.repository.TodoRepository

class GetTodosUseCase(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(): List<TodoItem> = repository.getTodos()
}
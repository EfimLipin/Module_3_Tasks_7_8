package com.example.module_3_tasks_7_8.domain.usecase

import com.example.module_3_tasks_7_8.domain.repository.TodoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class ToggleTodoUseCaseTest {

    @Test
    fun `toggleTodo меняет isCompleted`() = runTest {
        val mockRepository = mock<TodoRepository>()
        val useCase = ToggleTodoUseCase(mockRepository)
        val todoId = 1

        useCase(todoId)

        verify(mockRepository).toggleTodo(todoId)
    }
}
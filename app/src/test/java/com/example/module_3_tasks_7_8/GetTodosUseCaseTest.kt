package com.example.module_3_tasks_7_8.domain.usecase

import com.example.module_3_tasks_7_8.domain.model.TodoItem
import com.example.module_3_tasks_7_8.domain.repository.TodoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetTodosUseCaseTest {

    @Test
    fun `GetTodosUseCase возвращает 3 задачи`() = runTest {
        val mockRepository = mock<TodoRepository>()

        val testTodos = listOf(
            TodoItem(1, "Task 1", "Description 1", false),
            TodoItem(2, "Task 2", "Description 2", true),
            TodoItem(3, "Task 3", "Description 3", false)
        )

        whenever(mockRepository.getTodos()).thenReturn(testTodos)

        val useCase = GetTodosUseCase(mockRepository)

        val result = useCase()

        assertEquals(3, result.size)
        assertEquals("Task 1", result[0].title)
        assertEquals("Task 2", result[1].title)
        assertEquals("Task 3", result[2].title)
    }
}
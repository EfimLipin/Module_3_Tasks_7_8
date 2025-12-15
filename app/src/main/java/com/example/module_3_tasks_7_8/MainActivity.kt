/*package com.example.module_3_tasks_7_8

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

// ========== МОДЕЛЬ ДАННЫХ ==========
data class TodoItem(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)

// ========== ВЬЮМОДЕЛЬ ==========
class TodoViewModel(context: Context) : ViewModel() {

    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val todos: StateFlow<List<TodoItem>> = _todos.asStateFlow()

    // Для простой навигации без Navigation Component
    private val _currentScreen = MutableStateFlow<TodoScreen>(TodoScreen.List)
    val currentScreen: StateFlow<TodoScreen> = _currentScreen.asStateFlow()

    private var _selectedTodoId = MutableStateFlow<Int?>(null)
    val selectedTodoId: StateFlow<Int?> = _selectedTodoId.asStateFlow()

    init {
        loadTodos(context)
    }

    private fun loadTodos(context: Context) {
        viewModelScope.launch {
            try {
                val json = context.assets.open("todos.json").bufferedReader().use {
                    it.readText()
                }
                val type = object : TypeToken<List<TodoItem>>() {}.type
                val loadedTodos = Gson().fromJson<List<TodoItem>>(json, type) ?: emptyList()
                _todos.value = loadedTodos
            } catch (e: IOException) {
                // Fallback to sample data if file not found
                _todos.value = listOf(
                    TodoItem(1, "Купить молоко", "2 литра, обезжиренное", false),
                    TodoItem(2, "Позвонить маме", "Спросить про выходные", true),
                    TodoItem(3, "Сделать ДЗ по Android", "Clean Architecture + Compose", false)
                )
            }
        }
    }

    fun toggleTodo(id: Int) {
        _todos.value = _todos.value.map { todo ->
            if (todo.id == id) todo.copy(isCompleted = !todo.isCompleted)
            else todo
        }
    }

    fun navigateToDetail(todoId: Int) {
        _selectedTodoId.value = todoId
        _currentScreen.value = TodoScreen.Detail
    }

    fun navigateBack() {
        _currentScreen.value = TodoScreen.List
        _selectedTodoId.value = null
    }
}

// ========== ЭКРАНЫ ДЛЯ НАВИГАЦИИ ==========
sealed class TodoScreen {
    object List : TodoScreen()
    object Detail : TodoScreen()
}

// ========== КОМПОЗАБЛ ЭКРАНЫ ==========
@Composable
fun TodoListScreen(
    viewModel: TodoViewModel,
    onItemClick: (Int) -> Unit
) {
    val todos by viewModel.todos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Список задач (${todos.size})",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        if (todos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет задач для отображения")
            }
        } else {
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
}

@Composable
fun TodoItemCard(
    todo: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = onCheckedChange
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun TodoDetailScreen(
    viewModel: TodoViewModel,
    todoId: Int
) {
    val todos by viewModel.todos.collectAsState()
    val todo = todos.find { it.id == todoId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { viewModel.navigateBack() },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Назад")
        }

        if (todo != null) {
            Column {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "Статус: ${if (todo.isCompleted) "Выполнена" else "Не выполнена"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (todo.isCompleted) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Задача не найдена")
            }
        }
    }
}

// ========== ГЛАВНЫЙ КОМПОЗАБЛ ==========
@Composable
fun TodoApp(viewModel: TodoViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedTodoId by viewModel.selectedTodoId.collectAsState()

    when (currentScreen) {
        is TodoScreen.List -> {
            TodoListScreen(
                viewModel = viewModel,
                onItemClick = { todoId -> viewModel.navigateToDetail(todoId) }
            )
        }
        is TodoScreen.Detail -> {
            if (selectedTodoId != null) {
                TodoDetailScreen(
                    viewModel = viewModel,
                    todoId = selectedTodoId!!
                )
            } else {
                // Если нет выбранной задачи, возвращаемся к списку
                TodoListScreen(
                    viewModel = viewModel,
                    onItemClick = { todoId -> viewModel.navigateToDetail(todoId) }
                )
            }
        }
    }
}

// ========== ТЕМА ==========
@Composable
fun TodoListTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        content = content
    )
}

// ========== MAIN ACTIVITY ==========
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TodoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Создаем и передаем ViewModel
                    val viewModel = remember {
                        TodoViewModel(this)
                    }
                    TodoApp(viewModel = viewModel)
                }
            }
        }
    }
}*/


package com.example.module_3_tasks_7_8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.module_3_tasks_7_8.data.local.TodoJsonDataSource
import com.example.module_3_tasks_7_8.data.repository.TodoRepositoryImpl
import com.example.module_3_tasks_7_8.domain.usecase.GetTodosUseCase
import com.example.module_3_tasks_7_8.domain.usecase.ToggleTodoUseCase
import com.example.module_3_tasks_7_8.navigation.NavGraph
import com.example.module_3_tasks_7_8.presentation.viewmodel.TodoViewModel
import com.example.module_3_tasks_7_8.ui.theme.TodoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TodoListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent() {
    val context = LocalContext.current

    val viewModel: TodoViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dataSource = TodoJsonDataSource(context)
                val repository = TodoRepositoryImpl(dataSource)
                val getTodosUseCase = GetTodosUseCase(repository)
                val toggleTodoUseCase = ToggleTodoUseCase(repository)

                return TodoViewModel(
                    getTodosUseCase = getTodosUseCase,
                    toggleTodoUseCase = toggleTodoUseCase,
                    repository = repository
                ) as T
            }
        }
    )

    NavGraph()
}

package com.example.module_3_tasks_7_8.data.local

import android.content.Context
import com.example.module_3_tasks_7_8.data.model.TodoItemDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TodoJsonDataSource(private val context: Context) {
    private val gson = Gson()

    fun getTodos(): List<TodoItemDto> {
        return try {
            val json = context.assets.open("todos.json").bufferedReader().use {
                it.readText()
            }
            val type = object : TypeToken<List<TodoItemDto>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
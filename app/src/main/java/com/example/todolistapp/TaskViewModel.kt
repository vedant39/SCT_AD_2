package com.example.todolistapp

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


data class Task(
    val id: Int,
    var title: String,
    var completed: Boolean = false
)


class TaskViewModel : ViewModel() {
    private var nextId = 1


    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()


    fun addTask(title: String) {
        _tasks.value += Task(nextId++, title)
    }


    fun deleteTask(task: Task) {
        _tasks.value = _tasks.value.filter { it.id != task.id }
    }


    fun updateTask(task: Task, newTitle: String) {
        _tasks.value = _tasks.value.map {
            if (it.id == task.id) it.copy(title = newTitle) else it
        }
    }


    fun toggleTaskCompletion(task: Task) {
        _tasks.value = _tasks.value.map {
            if (it.id == task.id) it.copy(completed = !it.completed) else it
        }
    }
}

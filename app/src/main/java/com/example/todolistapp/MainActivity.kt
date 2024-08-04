package com.example.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.todolistapp.ui.theme.TodolistAppTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment


class MainActivity : ComponentActivity() {
    private val viewModel = TaskViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodolistAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskList(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun TaskList(viewModel: TaskViewModel, modifier: Modifier = Modifier) {
    // Observe the task list from the ViewModel
    val tasks by viewModel.tasks.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        TaskInput(viewModel)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onDelete = { viewModel.deleteTask(it) },
                    onEdit = { newTitle -> viewModel.updateTask(task, newTitle) },
                    onToggleCompletion = { viewModel.toggleTaskCompletion(it) }
                )
            }
        }
    }
}


@Composable
fun TaskInput(viewModel: TaskViewModel) {
    var text by remember { mutableStateOf(TextFieldValue()) }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            label = { Text("New Task") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            if (text.text.isNotBlank()) {
                viewModel.addTask(text.text)
                text = TextFieldValue()
            }
        }) {
            Text("Add")
        }
    }
}


@Composable
fun TaskItem(
    task: Task,
    onDelete: (Task) -> Unit,
    onEdit: (String) -> Unit,
    onToggleCompletion: (Task) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf(TextFieldValue(task.title)) }

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (task.completed) Color.Green else Color.Transparent),
        headlineContent = {
            if (isEditing) {
                TextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Edit Task") }
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = task.completed,
                        onCheckedChange = { checked ->
                            onToggleCompletion(task.copy(completed = checked))
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(task.title)
                }
            }
        },
        trailingContent = {
            Row {
                IconButton(onClick = {
                    if (isEditing) {
                        onEdit(newTitle.text)
                    }
                    isEditing = !isEditing
                }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { onDelete(task) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewTaskList() {
    TodolistAppTheme {
        TaskList(viewModel = TaskViewModel())
    }
}

package com.jonasbina.cleantodo.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonasbina.cleantodo.model.Todo
import com.jonasbina.cleantodo.model.TodoEvent
import com.jonasbina.cleantodo.model.TodoState

@Composable
fun TodoScreen(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(TodoEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add todo"
                )
            }
        },
    ) { p ->
        val pad = p
        if (state.isAddingTodo) {
            AddTodoDialog(state = state, onEvent = onEvent)
        }
        if (state.isEditingTodo) {
            EditTodoDialog(state = state, onEvent = onEvent, context = LocalContext.current)
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(text = "CleanTodo", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            }

            val categories = mapOf("Priority" to Pair(Color.Red,state.todosPriority),"Doing" to Pair(Color.Yellow,state.todosDoing),"ToDo" to Pair(Color.Cyan,state.todosTodo),"Done" to Pair(Color.Green,state.todosDone))
            for (e in categories){
            item {
                DisplayTodoCategoryHeader(categoryName = e.key, categoryColor = e.value.first)
            }
            items(e.value.second) { todo ->
                DisplayTodo(todo = todo, onEvent = onEvent)
            }
            }

        }
    }
}

@Composable
fun DisplayTodoCategoryHeader(categoryName: String, categoryColor: Color) {
    Row(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(categoryColor)
        )
        Spacer(modifier = Modifier.width(40.dp))
        Text(text = categoryName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DisplayTodo(todo:Todo, onEvent: (TodoEvent) -> Unit) {

    Column(
        modifier = Modifier
            .clickable {
                onEvent(TodoEvent.ShowEditDialog(todo))
            }
            .fillMaxWidth()
    ) {
        Text(

            text = todo.title,
            fontSize = 20.sp,
        )
        Text(text = todo.description, fontSize = 12.sp)
    }
}
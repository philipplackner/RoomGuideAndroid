package com.jonasbina.cleantodo

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        if(state.isAddingTodo) {
            AddTodoDialog(state = state, onEvent = onEvent)
        }
        if (state.isEditingTodo){
            EditTodoDialog(state = state, onEvent = onEvent, context = LocalContext.current)
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(text = "CleanTodo")
            }
            items(state.todos) { todo ->

                    Column(
                        modifier = Modifier
                            .clickable {
                                onEvent(TodoEvent.ShowEditDialog(todo))
                                Log.e("clickhehe", "ten magor na me clicknul. State : $state")
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
        }
    }
}
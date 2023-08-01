package com.jonasbina.cleantodo

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditTodoDialog(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    modifier: Modifier = Modifier,
    context: Context
) {
    var stateOfTodo by remember {
        mutableStateOf(state.editedTodo!!.state)
    }
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(TodoEvent.HideEditDialog)
            onEvent(TodoEvent.SetTitle(""))
            onEvent(TodoEvent.SetDescription(""))
        },
        title = { Text(text = "Edited todo") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.title,
                    onValueChange = {
                        onEvent(TodoEvent.SetTitle(it))
                    },
                    placeholder = {
                        Text(text = "Title")
                    }
                )
                TextField(
                    value = state.description,
                    onValueChange = {
                        onEvent(TodoEvent.SetDescription(it))
                    },
                    placeholder = {
                        Text(text = "Description")
                    }
                )
                val options =
                    mapOf(0 to "To Start", 1 to "Already doing", 2 to "Priority", 3 to "Done")

                Column {
                    for (o in options) {


                        Row {
                            RadioButton(
                                selected = (stateOfTodo == o.key),
                                onClick = { stateOfTodo = o.key }
                            )
                            Text(
                                text = o.value,
                                style = MaterialTheme.typography.body1.merge(),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }

        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(modifier=Modifier.width(100.dp),onClick = {
                    val todo = state.editedTodo


                    onEvent(TodoEvent.UpdateTodo(title = state.title, description = state.description,state=stateOfTodo, uid = todo!!.id))
                    onEvent(TodoEvent.HideEditDialog)
                }) {
                    Text(text = "Save")
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(modifier=Modifier.width(100.dp),onClick = {
                    onEvent(TodoEvent.DeleteTodo(state.editedTodo!!))
                    onEvent(TodoEvent.HideEditDialog)
                }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text(text = "Delete")
                }
            }

        }
    )
}

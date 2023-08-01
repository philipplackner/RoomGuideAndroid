package com.jonasbina.cleantodo.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasbina.cleantodo.model.Todo
import com.jonasbina.cleantodo.model.TodoDao
import com.jonasbina.cleantodo.model.TodoEvent
import com.jonasbina.cleantodo.model.TodoState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TodoViewModel(
    private val dao: TodoDao
): ViewModel() {


    private val _todos =dao.getTodos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    var todosTodo = _todos.value.filter { it.state == 0 }
    var todosDoing = _todos.value.filter { it.state == 1 }
    var todosPriority = _todos.value.filter { it.state == 2 }
    var todosDone = _todos.value.filter { it.state == 3 }

    private val _state = MutableStateFlow(TodoState(todosPriority = todosPriority, todosTodo = todosTodo, todosDone = todosDone, todosDoing = todosDoing))
    val state = combine(_state, _todos) { state, todos ->
        state.copy(
            todos = todos,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoState(todosPriority = todosPriority, todosTodo = todosTodo, todosDone = todosDone, todosDoing = todosDoing))

    fun onEvent(event: TodoEvent) {
        when(event) {
            is TodoEvent.DeleteTodo -> {
                viewModelScope.launch {
                    dao.deleteTodo(event.todo)
                }
            }
            TodoEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingTodo = false
                ) }
            }
            TodoEvent.SaveTodo -> {
                val title = state.value.title
                val description = state.value.description


                if(title.isBlank() || description.isBlank()) {
                    return
                }

                val todo = Todo(
                    title = title,
                    description=description
                )
                viewModelScope.launch {
                    dao.upsertTodo(todo)
                }
                _state.update { it.copy(
                    isAddingTodo = false,
                    title = "",
                    description = ""
                ) }
            }
            is TodoEvent.SetTitle -> {
                _state.update { it.copy(
                    title = event.title
                ) }
            }
            is TodoEvent.SetDescription -> {
                _state.update { it.copy(
                    description = event.description
                ) }
            }

            is TodoEvent.ShowEditDialog->{
                _state.update { it.copy(
                    isEditingTodo = true,
                    editedTodo = event.todo,
                    title = event.todo.title,
                    description = event.todo.description
                ) }
            }
            is TodoEvent.HideEditDialog->{
                _state.update { it.copy(
                    isEditingTodo = false,
                    editedTodo = null,
                    title = "",
                    description =""
                ) }
            }
            is TodoEvent.UpdateTodo ->{
                viewModelScope.launch(Dispatchers.IO) {
                    dao.updateTodoDescription(event.uid,event.description)
                    dao.updateTodoTitle(event.uid,event.title)
                    dao.updateTodoState(event.uid,event.state)
                }
            }
            TodoEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingTodo = true
                ) }
            }



        }
    }
}
package com.jonasbina.cleantodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TodoViewModel(
    private val dao: TodoDao
): ViewModel() {


    private val _todos =dao.getTodos().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(TodoState())
    val state = combine(_state, _todos) { state, todos ->
        state.copy(
            todos = todos,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodoState())

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
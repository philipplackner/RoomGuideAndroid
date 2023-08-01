package com.jonasbina.cleantodo

data class TodoState(
    val todos: List<Todo> = emptyList(),
    val title: String = "",
    val description: String = "",
    val isAddingTodo: Boolean = false,
    val isEditingTodo : Boolean = false,
    val editedTodo : Todo? = null
)

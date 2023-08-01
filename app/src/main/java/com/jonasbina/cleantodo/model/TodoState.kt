package com.jonasbina.cleantodo.model

data class TodoState(
    val todos: List<Todo> = emptyList(),
    val title: String = "",
    val description: String = "",
    val isAddingTodo: Boolean = false,
    val isEditingTodo : Boolean = false,
    val editedTodo : Todo? = null,
    val todosTodo:List<Todo>,
    val todosPriority:List<Todo>,
    val todosDone:List<Todo>,
    val todosDoing:List<Todo>
)

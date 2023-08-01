package com.jonasbina.cleantodo

sealed interface TodoEvent {
    object SaveTodo : TodoEvent
    data class SetTitle(val title: String) : TodoEvent
    data class SetDescription(val description: String) : TodoEvent
    object ShowDialog : TodoEvent
    object HideDialog : TodoEvent
    data class DeleteTodo(val todo: Todo) : TodoEvent
    data class ShowEditDialog(val todo: Todo) : TodoEvent
    object HideEditDialog : TodoEvent
    data class UpdateTodo(val title: String, val description: String, val state: Int, val uid : Long) : TodoEvent

}
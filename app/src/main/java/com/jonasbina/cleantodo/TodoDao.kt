package com.jonasbina.cleantodo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Upsert
    suspend fun upsertTodo(todo: Todo)

    @Query("UPDATE Todo SET title = :title WHERE id = :tid ")
    fun updateTodoTitle(tid: Long, title: String): Int
    @Query("UPDATE Todo SET description = :description WHERE id = :tid ")
    fun updateTodoDescription(tid: Long, description: String): Int
    @Query("UPDATE Todo SET state = :state WHERE id = :tid ")
    fun updateTodoState(tid: Long, state: Int): Int
    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Query("SELECT * FROM Todo")
    fun getTodos(): Flow<List<Todo>>

}
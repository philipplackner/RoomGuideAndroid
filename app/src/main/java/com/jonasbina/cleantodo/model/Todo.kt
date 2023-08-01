package com.jonasbina.cleantodo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    val title: String,
    val description: String,
    val state:Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

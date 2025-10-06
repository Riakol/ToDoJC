package com.riakol.todojc.domain.model

data class SubTask(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false,
    val taskId: Int
)
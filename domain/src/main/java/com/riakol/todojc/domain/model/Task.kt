package com.riakol.todojc.domain.model

data class Task(
    val id: Int,
    val title: String,
    val description: String?,
    val creationDate: Long,
    val isCompleted: Boolean = false,
    val groupId: Int,
    val subTasks: List<SubTask> = emptyList()
)

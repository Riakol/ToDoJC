package com.riakol.todojc.domain.repository

import com.riakol.todojc.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasksForGroup(groupId: Int): Flow<List<Task>>

    fun getTaskById(taskId: Int): Flow<Task?>

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun deleteMultipleTask(tasks: List<Int>)
}
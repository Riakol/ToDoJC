package com.riakol.todojc.domain.repository

import com.riakol.todojc.domain.model.SubTask
import kotlinx.coroutines.flow.Flow

interface SubTaskRepository {
    fun getSubTasksForTask(taskId: Int): Flow<List<SubTask>>
    suspend fun addSubTask(subTask: SubTask)
    suspend fun updateSubTask(subTask: SubTask)
    suspend fun deleteSubTask(subTask: SubTask)
}
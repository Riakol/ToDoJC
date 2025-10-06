package com.riakol.todojc.data.repository

import com.riakol.todojc.data.local.dao.SubTaskListDao
import com.riakol.todojc.data.mapper.toSubTask
import com.riakol.todojc.data.mapper.toSubTaskList
import com.riakol.todojc.domain.model.SubTask
import com.riakol.todojc.domain.repository.SubTaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubTaskRepositoryImpl @Inject constructor(
    private val subTaskListDao: SubTaskListDao
) : SubTaskRepository {
    override fun getSubTasksForTask(taskId: Int): Flow<List<SubTask>> {
        return subTaskListDao.getSubTasksForTask(taskId).map { subTasks ->
            subTasks.map { it.toSubTask() }
        }
    }

    override suspend fun addSubTask(subTask: SubTask) {
        subTaskListDao.addSubTask(subTask.toSubTaskList())
    }

    override suspend fun updateSubTask(subTask: SubTask) {
        subTaskListDao.updateSubTask(subTask.toSubTaskList())
    }

    override suspend fun deleteSubTask(subTask: SubTask) {
        subTaskListDao.deleteSubTask(subTask.toSubTaskList())
    }
}
package com.riakol.todojc.data.repository

import com.riakol.todojc.data.local.dao.TaskDao
import com.riakol.todojc.data.local.entity.TaskList
import com.riakol.todojc.data.mapper.toTask
import com.riakol.todojc.data.mapper.toTaskList
import com.riakol.todojc.domain.model.Task
import com.riakol.todojc.domain.repository.TaskRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override fun getTasksForGroup(groupId: Int): Flow<List<Task>> {
        return taskDao.getTasksForGroup(groupId).map { taskList ->
            taskList.map { it.toTask() }
        }
    }

    override fun getTaskById(taskId: Int): Flow<Task> {
        return taskDao.getTaskById(taskId).map { it.toTask() }
    }

    override suspend fun addTask(task: Task) {
        taskDao.addTask(task.toTaskList())
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }

}
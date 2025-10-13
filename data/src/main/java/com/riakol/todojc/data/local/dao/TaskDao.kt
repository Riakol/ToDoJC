package com.riakol.todojc.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.riakol.todojc.data.local.entity.TaskList
import com.riakol.todojc.data.local.relations.CategoryWithGroups
import com.riakol.todojc.data.local.relations.TaskWithSubTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE group_id = :groupId")
    fun getTasksForGroup(groupId: Int): Flow<List<TaskList>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): Flow<TaskList?>

    @Insert
    suspend fun addTask(task: TaskList)

    @Update
    suspend fun updateTask(task: TaskList)

    @Delete
    suspend fun deleteTask(task: TaskList)

    @Transaction
    @Query("SELECT * FROM tasks WHERE group_id = :groupId")
    fun getTasksWithSubTasks(groupId: Int): Flow<List<TaskWithSubTasks>>
}
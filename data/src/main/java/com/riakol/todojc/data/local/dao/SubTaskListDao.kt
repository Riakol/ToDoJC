package com.riakol.todojc.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.riakol.todojc.data.local.entity.SubTaskList
import kotlinx.coroutines.flow.Flow

@Dao
interface SubTaskListDao {
    @Query("SELECT * FROM sub_tasks WHERE task_id = :taskId")
    fun getSubTasksForTask(taskId: Int): Flow<List<SubTaskList>>

    @Delete
    suspend fun deleteSubTask(subTaskList: SubTaskList)

    @Insert
    suspend fun addSubTask(subTaskList: SubTaskList)

    @Update
    suspend fun updateSubTask(subTaskList: SubTaskList)
}
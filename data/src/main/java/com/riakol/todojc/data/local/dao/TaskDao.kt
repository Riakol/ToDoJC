package com.riakol.todojc.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.riakol.todojc.data.local.entity.TaskList
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE group_id = :groupId")
    fun getTasksForGroup(groupId: Int): Flow<List<TaskList>>

    @Insert
    suspend fun addTask(task: TaskList)
}
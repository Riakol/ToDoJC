package com.riakol.todojc.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.riakol.todojc.data.local.entity.GroupList
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupListDao {
    @Query("SELECT * FROM group_lists")
    fun getAllGroups(): Flow<List<GroupList>>

    @Query("SELECT * FROM group_lists WHERE category_id IS NULL")
    fun getUnassignedGroups(): Flow<List<GroupList>>

    @Query("SELECT * FROM group_lists WHERE id = :groupId")
    fun getGroupById(groupId: Int): Flow<GroupList>

    @Insert
    suspend fun addGroup(groupList: GroupList)

    @Delete
    suspend fun deleteGroup(groupList: GroupList)

    @Update
    suspend fun updateGroup(groupList: GroupList)
}
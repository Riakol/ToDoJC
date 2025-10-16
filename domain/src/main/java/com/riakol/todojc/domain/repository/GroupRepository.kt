package com.riakol.todojc.domain.repository

import com.riakol.todojc.domain.model.Group
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getGroups(): Flow<List<Group>>
    fun getUnassignedGroups(): Flow<List<Group>>
    suspend fun addGroup(group: Group)
    fun getGroupById(groupId: Int): Flow<Group>
    suspend fun deleteGroup(group: Group)
    suspend fun updateGroup(group: Group)
}
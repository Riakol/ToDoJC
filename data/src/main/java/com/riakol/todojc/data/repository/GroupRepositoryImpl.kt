package com.riakol.todojc.data.repository

import com.riakol.todojc.data.local.dao.GroupListDao
import com.riakol.todojc.data.mapper.toGroup
import com.riakol.todojc.data.mapper.toGroupList
import com.riakol.todojc.domain.model.Group
import com.riakol.todojc.domain.repository.GroupRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GroupRepositoryImpl @Inject constructor(
    private val dao: GroupListDao
) : GroupRepository {
    override fun getGroups(): Flow<List<Group>> {
        return dao.getAllGroups().map { groupLists ->
            groupLists.map { groupList ->
                groupList.toGroup()
            }
        }
    }

    override fun getUnassignedGroups(): Flow<List<Group>> {
        return dao.getUnassignedGroups().map { groupLists ->
            groupLists.map { groupList ->
                groupList.toGroup()
            }
        }
    }


    override suspend fun addGroup(group: Group) {
        dao.addGroup(group.toGroupList())
    }

    override fun getGroupById(groupId: Int): Flow<Group> {
        return dao.getGroupById(groupId).map { groupList -> groupList.toGroup() }
    }

}
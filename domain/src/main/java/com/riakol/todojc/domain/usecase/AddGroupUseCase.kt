package com.riakol.todojc.domain.usecase

import com.riakol.todojc.domain.model.Group
import com.riakol.todojc.domain.repository.GroupRepository
import javax.inject.Inject

class AddGroupUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    suspend operator fun invoke(group: Group) {
        repository.addGroup(group)
    }
}
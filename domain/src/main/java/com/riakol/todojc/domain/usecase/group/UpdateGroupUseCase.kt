package com.riakol.todojc.domain.usecase.group

import com.riakol.todojc.domain.model.Group
import com.riakol.todojc.domain.repository.GroupRepository
import javax.inject.Inject

class UpdateGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(group: Group) {
        groupRepository.updateGroup(group)
    }
}
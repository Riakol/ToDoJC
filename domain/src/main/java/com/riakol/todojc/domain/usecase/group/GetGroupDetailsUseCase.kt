package com.riakol.todojc.domain.usecase.group

import com.riakol.todojc.domain.repository.GroupRepository
import jakarta.inject.Inject

class GetGroupDetailsUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    operator fun invoke(groupId: Int) = groupRepository.getGroupById(groupId)
}
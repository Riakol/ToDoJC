package com.riakol.todojc.domain.usecase.group

import com.riakol.todojc.domain.repository.GroupRepository
import jakarta.inject.Inject

class GetUnassignedGroupsUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    operator fun invoke() = repository.getUnassignedGroups()
}

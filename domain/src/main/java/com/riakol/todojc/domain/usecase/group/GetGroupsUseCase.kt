package com.riakol.todojc.domain.usecase.group

import com.riakol.todojc.domain.repository.GroupRepository
import jakarta.inject.Inject

class GetGroupsUseCase @Inject constructor(
    private val repository: GroupRepository
) {
    operator fun invoke() = repository.getGroups()
}
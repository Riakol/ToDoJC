package com.riakol.todojc.domain.usecase.task

import com.riakol.todojc.domain.repository.TaskRepository
import jakarta.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(groupId: Int) = repository.getTasksForGroup(groupId)
}
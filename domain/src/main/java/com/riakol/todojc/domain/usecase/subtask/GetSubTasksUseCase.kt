package com.riakol.todojc.domain.usecase.subtask

import com.riakol.todojc.domain.repository.SubTaskRepository
import jakarta.inject.Inject

class GetSubTasksUseCase @Inject constructor(
    private val subTaskRepository: SubTaskRepository
) {
    operator fun invoke(taskId: Int) = subTaskRepository.getSubTasksForTask(taskId)
}
package com.riakol.todojc.domain.usecase.task

import com.riakol.todojc.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskDetailsUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(taskId: Int) = taskRepository.getTaskById(taskId)
}
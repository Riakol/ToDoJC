package com.riakol.todojc.domain.usecase.task

import com.riakol.todojc.domain.repository.TaskRepository
import javax.inject.Inject

class RemoveMultipleTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(tasks: List<Int>) = taskRepository.deleteMultipleTask(tasks)
}
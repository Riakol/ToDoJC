package com.riakol.todojc.domain.usecase.task

import com.riakol.todojc.domain.model.Task
import com.riakol.todojc.domain.repository.TaskRepository
import jakarta.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        taskRepository.updateTask(task)
    }
}
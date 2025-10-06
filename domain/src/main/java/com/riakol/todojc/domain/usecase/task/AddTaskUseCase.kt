package com.riakol.todojc.domain.usecase.task

import com.riakol.todojc.domain.model.Task
import com.riakol.todojc.domain.repository.TaskRepository
import jakarta.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.addTask(task)
    }
}
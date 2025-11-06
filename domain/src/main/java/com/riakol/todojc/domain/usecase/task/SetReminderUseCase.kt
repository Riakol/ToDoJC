package com.riakol.todojc.domain.usecase.task

import com.riakol.todojc.domain.model.Task
import com.riakol.todojc.domain.repository.TaskRepository
import javax.inject.Inject

class SetReminderUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task, reminderDate: Long?) {
        taskRepository.updateTask(task.copy(reminderDate = reminderDate))
    }
}
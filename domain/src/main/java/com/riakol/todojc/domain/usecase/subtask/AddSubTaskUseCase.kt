package com.riakol.todojc.domain.usecase.subtask

import com.riakol.todojc.domain.model.SubTask
import com.riakol.todojc.domain.repository.SubTaskRepository
import jakarta.inject.Inject

class AddSubTaskUseCase @Inject constructor(
    private val subTaskRepository: SubTaskRepository
) {
    suspend operator fun invoke(subTask: SubTask) {
        subTaskRepository.addSubTask(subTask)
    }
}
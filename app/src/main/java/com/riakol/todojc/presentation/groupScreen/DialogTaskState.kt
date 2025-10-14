package com.riakol.todojc.presentation.groupScreen

import com.riakol.todojc.domain.model.Task

sealed interface DialogTaskState {
    data object None: DialogTaskState
    data class RenameTask(val taskId: Int): DialogTaskState
    data class RemoveTask(val task: Task): DialogTaskState
    data object AddNewTask: DialogTaskState
}
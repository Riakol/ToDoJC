package com.riakol.todojc.presentation.groupScreen

sealed interface DialogTaskState {
    data object None: DialogTaskState
    data class RenameTask(val taskId: Int): DialogTaskState
    data object AddNewTask: DialogTaskState
}
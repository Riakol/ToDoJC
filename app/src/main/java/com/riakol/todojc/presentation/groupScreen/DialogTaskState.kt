package com.riakol.todojc.presentation.groupScreen

import com.riakol.todojc.domain.model.Group
import com.riakol.todojc.domain.model.Task

sealed interface DialogTaskState {
    data object None: DialogTaskState
    data class RenameTask(val taskId: Int): DialogTaskState
    data class RemoveTask(val task: Task): DialogTaskState
    data object AddNewTask: DialogTaskState
    data class RenameGroup(val groupId: Int): DialogTaskState
    data class RemoveGroup(val group: Group): DialogTaskState
    data class RemoveMultipleTasks(val tasksId: Set<Int>): DialogTaskState
}
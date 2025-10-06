package com.riakol.todojc.data.mapper

import com.riakol.todojc.data.local.entity.SubTaskList
import com.riakol.todojc.domain.model.SubTask

fun SubTask.toSubTaskList() : SubTaskList {
    return SubTaskList(
        id = this.id,
        title = this.title,
        isCompleted = this.isCompleted,
        taskId = this.taskId
    )
}

fun SubTaskList.toSubTask() : SubTask {
    return SubTask(
        id = this.id,
        title = this.title,
        isCompleted = this.isCompleted,
        taskId = this.taskId
    )
}
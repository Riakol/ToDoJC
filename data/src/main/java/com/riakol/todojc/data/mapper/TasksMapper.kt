package com.riakol.todojc.data.mapper

import com.riakol.todojc.data.local.entity.TaskList
import com.riakol.todojc.domain.model.Task

fun TaskList.toTask(): Task {
    return Task(
        id = this.id,
        title = this.title,
        description = this.description,
        dueDate = this.dueDate,
        isCompleted = this.isCompleted,
        groupId = this.groupId
    )
}

fun Task.toTaskList(): TaskList {
    return TaskList(
        id = this.id,
        title = this.title,
        description = this.description,
        dueDate = this.dueDate,
        isCompleted = this.isCompleted,
        groupId = this.groupId
    )
}
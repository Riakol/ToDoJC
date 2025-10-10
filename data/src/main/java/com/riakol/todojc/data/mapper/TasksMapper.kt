package com.riakol.todojc.data.mapper

import com.riakol.todojc.data.local.entity.TaskList
import com.riakol.todojc.data.local.relations.TaskWithSubTasks
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

fun TaskWithSubTasks.toTask(): Task {
    return Task(
        id = this.task.id,
        title = this.task.title,
        description = this.task.description,
        dueDate = this.task.dueDate,
        isCompleted = this.task.isCompleted,
        groupId = this.task.groupId,
        subTasks = this.subTasks.map { it.toSubTask() }
    )
}

fun Task.toTaskWithSubTasks(): TaskWithSubTasks {
    return TaskWithSubTasks(
        task = TaskList(
            id = this.id,
            title = this.title,
            description = this.description,
            dueDate = this.dueDate,
            isCompleted = this.isCompleted,
            groupId = this.groupId
        ),
        subTasks = this.subTasks.map { it.toSubTaskList() }
    )
}
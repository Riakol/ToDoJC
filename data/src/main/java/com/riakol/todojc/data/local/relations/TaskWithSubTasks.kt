package com.riakol.todojc.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.riakol.todojc.data.local.entity.SubTaskList
import com.riakol.todojc.data.local.entity.TaskList

data class TaskWithSubTasks(
    @Embedded val task: TaskList,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val subTasks: List<SubTaskList>
)
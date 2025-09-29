package com.riakol.todojc.data.mapper

import com.riakol.todojc.data.local.entity.GroupList
import com.riakol.todojc.domain.model.Group

fun GroupList.toGroup(): Group {
    return Group(
        id = this.id,
        name = this.name,
        categoryId = this.categoryId
    )
}

fun Group.toGroupList(): GroupList {
    return GroupList(
        id = this.id,
        name = this.name,
        categoryId = this.categoryId
    )
}
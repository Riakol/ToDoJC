package com.riakol.todojc.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.riakol.todojc.data.local.entity.CategoryList
import com.riakol.todojc.data.local.entity.GroupList

data class CategoryWithGroups(
    @Embedded val category: CategoryList,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val groups: List<GroupList>
)

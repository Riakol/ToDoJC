package com.riakol.todojc.data.mapper

import com.riakol.todojc.data.local.entity.CategoryList
import com.riakol.todojc.data.local.relations.CategoryWithGroups
import com.riakol.todojc.domain.model.Category

fun CategoryWithGroups.toCategory(): Category {
    return Category(
        id = this.category.id,
        name = this.category.name,
        groups = this.groups.map {it.toGroup()}
    )
}

fun Category.toCategoryWithGroups(): CategoryWithGroups {
    return CategoryWithGroups(
        category = CategoryList(
            id = this.id,
            name = this.name
        ),
        groups = this.groups.map {it.toGroupList()}
    )

}
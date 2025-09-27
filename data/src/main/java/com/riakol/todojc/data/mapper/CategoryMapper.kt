package com.riakol.todojc.data.mapper

import com.riakol.todojc.data.local.CategoryList
import com.riakol.todojc.domain.model.Category

fun CategoryList.toCategory(): Category {
    return Category(
        id = this.id,
        name = this.name
    )
}

fun Category.toCategoryList(): CategoryList {
    return CategoryList(
        id = this.id,
        name = this.name
    )
}
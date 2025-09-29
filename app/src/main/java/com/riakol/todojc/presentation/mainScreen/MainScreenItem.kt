package com.riakol.todojc.presentation.mainScreen

import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.domain.model.Group

sealed interface MainScreenItem {
    data class CategoryItem(val category: Category) : MainScreenItem
    data class GroupItem(val group: Group) : MainScreenItem
}
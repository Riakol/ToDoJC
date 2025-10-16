package com.riakol.todojc.presentation.mainScreen.components

import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.domain.model.Group

sealed interface DynamicListEvent {
    data class OnGroupClick(val groupId: Int) : DynamicListEvent
    data class OnRenameGroupClick(val group: Group) : DynamicListEvent
    data class OnDeleteGroupClick(val group: Group) : DynamicListEvent
    data class OnMoveGroupClick(val group: Group) : DynamicListEvent
    data class OnRenameCategoryClick(val category: Category) : DynamicListEvent
    data class OnAddNewGroupInListClick(val categoryId: Int) : DynamicListEvent
}
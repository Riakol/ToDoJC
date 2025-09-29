package com.riakol.todojc.presentation.mainScreen

import com.riakol.todojc.domain.model.Category

sealed interface DialogState{
    data object None: DialogState
    data object AddNewCategory: DialogState
    data class RenameCategory(val category: Category): DialogState
    data object AddNewUnassignedGroup: DialogState
    data class AddNewGroup(val categoryId: Int): DialogState
}
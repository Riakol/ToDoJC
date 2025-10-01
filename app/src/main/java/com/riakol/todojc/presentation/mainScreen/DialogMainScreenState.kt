package com.riakol.todojc.presentation.mainScreen

import com.riakol.todojc.domain.model.Category

sealed interface DialogMainScreenState {
    data object None: DialogMainScreenState
    data object AddNewCategory: DialogMainScreenState
    data class RenameCategory(val category: Category): DialogMainScreenState
    data object AddNewUnassignedGroup: DialogMainScreenState
    data class AddNewGroup(val categoryId: Int): DialogMainScreenState
}
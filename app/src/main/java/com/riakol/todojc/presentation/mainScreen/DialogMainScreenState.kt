package com.riakol.todojc.presentation.mainScreen

import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.domain.model.Group

sealed interface DialogMainScreenState {
    data object None: DialogMainScreenState
    data object AddNewCategory: DialogMainScreenState
    data class RenameCategory(val category: Category): DialogMainScreenState
    data class RemoveCategory(val category: Category): DialogMainScreenState
    data class RenameGroup(val group: Group): DialogMainScreenState
    data class RemoveGroup(val group: Group): DialogMainScreenState
    data class MoveGroup(val group: Group): DialogMainScreenState
    data object AddNewUnassignedGroup: DialogMainScreenState
    data class AddNewGroup(val categoryId: Int): DialogMainScreenState
}
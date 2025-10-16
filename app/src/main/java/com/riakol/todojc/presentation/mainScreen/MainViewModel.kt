package com.riakol.todojc.presentation.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.domain.model.Group
import com.riakol.todojc.domain.usecase.category.AddCategoryUseCase
import com.riakol.todojc.domain.usecase.group.AddGroupUseCase
import com.riakol.todojc.domain.usecase.category.GetCategoriesUseCase
import com.riakol.todojc.domain.usecase.category.UpdateCategoryUseCase
import com.riakol.todojc.domain.usecase.group.GetUnassignedGroupsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val addGroupUseCase: AddGroupUseCase,
    private val getUnassignedGroupsUseCase: GetUnassignedGroupsUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
) : ViewModel() {

    private val categoriesFlow = getCategoriesUseCase()
    private val unassignedGroupsFlow = getUnassignedGroupsUseCase()

    val mainScreenItems: StateFlow<List<MainScreenItem>> = combine(
        categoriesFlow,
        unassignedGroupsFlow
    ) { categories, unassignedGroups ->
        val combinedList = mutableListOf<MainScreenItem>()
        combinedList.addAll(
            unassignedGroups.map {
                MainScreenItem.GroupItem(it)
            }
        )
        combinedList.addAll(
            categories.map {
                MainScreenItem.CategoryItem(it)
            }
        )
        combinedList

    }.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000L),
        emptyList()
    )


    fun addCategory(name: String) {
        viewModelScope.launch {
            val newCategory = Category(
                id = 0,
                name = name
            )
            addCategoryUseCase(newCategory)
        }
    }

    fun addGroup(name: String, categoryId: Int) {
        viewModelScope.launch {
            val newGroup = Group(
                id = 0,
                name = name,
                categoryId = categoryId
            )
            addGroupUseCase(newGroup)
        }
    }

    fun addUnassignedGroup(name: String) {
        viewModelScope.launch {
            val newGroup = Group(
                id = 0,
                name = name,
                categoryId = null
            )
            addGroupUseCase(newGroup)
        }
    }

    fun renameCategory(category: Category, newName: String) {
        viewModelScope.launch {
            val newCategory = category.copy(name = newName)
            updateCategoryUseCase(newCategory)
        }
    }
}
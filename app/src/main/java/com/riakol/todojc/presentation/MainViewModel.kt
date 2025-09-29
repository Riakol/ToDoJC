package com.riakol.todojc.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.domain.model.Group
import com.riakol.todojc.domain.usecase.AddCategoryUseCase
import com.riakol.todojc.domain.usecase.AddGroupUseCase
import com.riakol.todojc.domain.usecase.GetCategoriesUseCase
import com.riakol.todojc.domain.usecase.GetGroupsUseCase
import com.riakol.todojc.domain.usecase.GetUnassignedGroupsUseCase
import com.riakol.todojc.presentation.mainScreen.MainScreenItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getGroupsUseCase: GetGroupsUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val addGroupUseCase: AddGroupUseCase,
    private val getUnassignedGroupsUseCase: GetUnassignedGroupsUseCase,
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups
    private val categoriesFlow = getCategoriesUseCase()
    private val unassignedGroupsFlow = getUnassignedGroupsUseCase()

    val mainScreenItems: StateFlow<List<MainScreenItem>> = combine(
        categoriesFlow,
        unassignedGroupsFlow
    ) {
        categories, unassignedGroups ->
        val combinedList = mutableListOf<MainScreenItem>()
        combinedList.addAll(unassignedGroups.map { MainScreenItem.GroupItem(it) })
        combinedList.addAll(categories.map { MainScreenItem.CategoryItem(it) })
        combinedList

    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())


    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { categories ->
                _categories.value = categories
            }
        }
    }

    private fun loadGroups() {
        viewModelScope.launch {
            getGroupsUseCase().collect { groups ->
                _groups.value = groups
            }
        }
    }

    fun addCategory(name: String) {
        viewModelScope.launch {
            val newCategory = Category(
                id = 0,
                name = name
            )
            addCategoryUseCase(newCategory)
        }
    }

    fun addGroup(name: String) {
        viewModelScope.launch {
            val newGroup = Group(
                id = 0,
                name = name,
                categoryId = 0
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
}
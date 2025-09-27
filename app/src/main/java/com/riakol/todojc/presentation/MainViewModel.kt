package com.riakol.todojc.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.domain.usecase.AddCategoryUseCase
import com.riakol.todojc.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

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

    fun addCategory(name: String) {
        viewModelScope.launch {
            val newCategory = Category(
                id = 0,
                name = name
            )
            addCategoryUseCase(newCategory)
        }
    }
}
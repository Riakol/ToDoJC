package com.riakol.todojc.domain.repository

import com.riakol.todojc.domain.model.CategoryList
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<CategoryList>>

    suspend fun addCategory(category: CategoryList)
}
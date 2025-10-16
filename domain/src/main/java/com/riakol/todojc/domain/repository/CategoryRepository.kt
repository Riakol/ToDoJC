package com.riakol.todojc.domain.repository

import com.riakol.todojc.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>

    suspend fun addCategory(category: Category)

    suspend fun updateCategory(category: Category)
}
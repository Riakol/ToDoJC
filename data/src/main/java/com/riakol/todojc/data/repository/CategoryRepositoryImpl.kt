package com.riakol.todojc.data.repository

import com.riakol.todojc.data.local.dao.CategoryListDao
import com.riakol.todojc.data.mapper.toCategory
import com.riakol.todojc.data.mapper.toCategoryWithGroups
import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.domain.repository.CategoryRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryListDao
) : CategoryRepository {
    override fun getCategories(): Flow<List<Category>> {
        return dao.getCategoriesWithGroups().map { list ->
            list.map {
                it.toCategory()
            }
        }
    }

    override suspend fun addCategory(category: Category) {
        dao.addCategory(category.toCategoryWithGroups().category)
    }

    override suspend fun updateCategory(category: Category) {
        dao.updateCategory(category.toCategoryWithGroups().category)
    }
}
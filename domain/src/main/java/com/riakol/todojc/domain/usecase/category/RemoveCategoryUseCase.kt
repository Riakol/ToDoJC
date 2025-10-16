package com.riakol.todojc.domain.usecase.category

import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.domain.repository.CategoryRepository
import javax.inject.Inject

class RemoveCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category) {
        categoryRepository.deleteCategory(category)
    }
}
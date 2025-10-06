package com.riakol.todojc.domain.usecase.category

import com.riakol.todojc.domain.model.Category
import com.riakol.todojc.domain.repository.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: Category) {
        repository.addCategory(category)
    }

}
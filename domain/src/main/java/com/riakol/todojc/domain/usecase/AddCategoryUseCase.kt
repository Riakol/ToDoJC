package com.riakol.todojc.domain.usecase

import com.riakol.todojc.domain.model.CategoryList
import com.riakol.todojc.domain.repository.CategoryRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(category: CategoryList) {
        repository.addCategory(category)
    }

}
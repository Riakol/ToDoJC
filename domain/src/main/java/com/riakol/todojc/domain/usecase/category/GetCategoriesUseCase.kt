package com.riakol.todojc.domain.usecase.category

import com.riakol.todojc.domain.repository.CategoryRepository
import jakarta.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke() = repository.getCategories()

}
package com.riakol.todojc.domain.model

data class Group(
    val id: Int,
    val name: String,
    val categoryId: Int?
)

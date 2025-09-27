package com.riakol.todojc.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.riakol.todojc.data.local.CategoryList
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryListDao {
    @Query("SELECT * FROM category_lists")
    fun getAllCategoryLists(): Flow<List<CategoryList>>

    @Insert
    suspend fun addCategory(categoryList: CategoryList)

    @Delete
    suspend fun deleteCategory(categoryList: CategoryList)

    @Update
    suspend fun updateCategory(categoryList: CategoryList)
}
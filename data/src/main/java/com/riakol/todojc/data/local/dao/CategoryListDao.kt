package com.riakol.todojc.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.riakol.todojc.data.local.entity.CategoryList
import com.riakol.todojc.data.local.relations.CategoryWithGroups
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryListDao {
    @Transaction
    @Query("SELECT * FROM category_lists")
    fun getCategoriesWithGroups(): Flow<List<CategoryWithGroups>>

    @Insert
    suspend fun addCategory(categoryList: CategoryList)

    @Delete
    suspend fun deleteCategory(categoryList: CategoryList)

    @Update
    suspend fun updateCategory(categoryList: CategoryList)
}
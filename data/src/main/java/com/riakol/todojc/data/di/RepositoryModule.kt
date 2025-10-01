package com.riakol.todojc.data.di

import com.riakol.todojc.data.repository.CategoryRepositoryImpl
import com.riakol.todojc.data.repository.GroupRepositoryImpl
import com.riakol.todojc.data.repository.TaskRepositoryImpl
import com.riakol.todojc.domain.repository.CategoryRepository
import com.riakol.todojc.domain.repository.GroupRepository
import com.riakol.todojc.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindGroupRepository(
        groupRepositoryImpl: GroupRepositoryImpl
    ): GroupRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

}
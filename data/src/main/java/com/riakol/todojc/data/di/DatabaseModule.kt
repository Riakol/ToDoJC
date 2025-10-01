package com.riakol.todojc.data.di

import android.content.Context
import androidx.room.Room
import com.riakol.todojc.data.local.dao.CategoryListDao
import com.riakol.todojc.data.local.dao.GroupListDao
import com.riakol.todojc.data.local.dao.TaskDao
import com.riakol.todojc.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryListDao {
        return database.categoryListDao()
    }

    @Provides
    fun provideGroupDao(database: AppDatabase): GroupListDao {
        return database.groupListDao()
    }

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskListDao()
    }


}
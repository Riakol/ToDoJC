package com.riakol.todojc.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.riakol.todojc.data.local.dao.CategoryListDao
import com.riakol.todojc.data.local.CategoryList
import com.riakol.todojc.data.local.GroupList
import com.riakol.todojc.data.local.SubTask
import com.riakol.todojc.data.local.Task

@Database(
    entities = [
        CategoryList::class,
        GroupList::class,
        Task::class,
        SubTask::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryListDao(): CategoryListDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
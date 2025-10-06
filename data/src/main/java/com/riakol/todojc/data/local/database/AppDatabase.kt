package com.riakol.todojc.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.riakol.todojc.data.local.dao.CategoryListDao
import com.riakol.todojc.data.local.dao.GroupListDao
import com.riakol.todojc.data.local.dao.SubTaskListDao
import com.riakol.todojc.data.local.dao.TaskDao
import com.riakol.todojc.data.local.entity.CategoryList
import com.riakol.todojc.data.local.entity.GroupList
import com.riakol.todojc.data.local.entity.SubTaskList
import com.riakol.todojc.data.local.entity.TaskList

@Database(
    entities = [
        CategoryList::class,
        GroupList::class,
        TaskList::class,
        SubTaskList::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryListDao(): CategoryListDao
    abstract fun groupListDao(): GroupListDao
    abstract fun taskListDao(): TaskDao
    abstract fun subTaskListDao(): SubTaskListDao

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
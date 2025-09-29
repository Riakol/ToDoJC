package com.riakol.todojc.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "group_lists",
    foreignKeys = [
        ForeignKey(
            entity = CategoryList::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GroupList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "group_name")
    val name: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Int?
)

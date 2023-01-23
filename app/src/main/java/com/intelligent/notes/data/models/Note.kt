package com.intelligent.notes.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.intelligent.notes.util.Constants.DATABASE_TABLE

@Entity(tableName = DATABASE_TABLE)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String
)
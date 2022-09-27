package com.example.noteit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class notesentity(
    @PrimaryKey(autoGenerate = true)
    val Id: Int = 0,
    val title: String="",
     val desp:String=""
)

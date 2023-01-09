package com.nda.simpletodo.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "noteTable")
data class Note(
        @PrimaryKey(autoGenerate = true) val nId: Int?,
        val nTitle: String,
        val nDescription: String,
        val nCreatedDate: String,
        val nIsFinish: Boolean,
        val nCategory: String
    )
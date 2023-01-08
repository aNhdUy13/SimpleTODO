package com.nda.simpletodo.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendarTable")
data class Calendar(
        @PrimaryKey(autoGenerate = true) val cId:Int?,
        val cTitle: String,
        val cDes: String,
        val nCreatedDate: String,
        val nIsFinish: Boolean,
    )
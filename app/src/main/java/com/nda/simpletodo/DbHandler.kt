package com.nda.simpletodo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nda.simpletodo.models.Calendar
import com.nda.simpletodo.models.Note


@Database(entities = [Note::class, Calendar::class], version = 1)
abstract class DbHandler : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "simpTODO.db"
        private var instance: DbHandler? = null

        @Synchronized
        fun getInstance(context: Context): DbHandler? {
            if (instance == null)
            {
                instance = Room.databaseBuilder(context.applicationContext, DbHandler::class.java, DATABASE_NAME)
                    .allowMainThreadQueries() // Cho phep Query tren MainThread
                    .build()
            }
            return instance
        }
    }

    abstract fun noteDao(): DAO?

}
package com.example.entrypass.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Attendee::class], version = 2, exportSchema = false)
abstract class EntryPassDb : RoomDatabase() {

    abstract fun attendeeDao(): AttendeeDao

    companion object {
        @Volatile private var INSTANCE: EntryPassDb? = null

        fun getInstance(context: Context): EntryPassDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EntryPassDb::class.java,
                    "entry_pass.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

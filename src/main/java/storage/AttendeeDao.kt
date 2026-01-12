package com.example.entrypass.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AttendeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(attendee: Attendee)

    @Query("SELECT * FROM attendees WHERE userId = :id LIMIT 1")
    suspend fun findById(id: Int): Attendee?

}

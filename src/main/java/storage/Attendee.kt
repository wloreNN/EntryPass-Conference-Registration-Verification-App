package com.example.entrypass.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendees")
data class Attendee(
    @PrimaryKey
    val userId: Int,

    val fullName: String,
    val title: String,
    val regType: String,
    val photoUri: String
)


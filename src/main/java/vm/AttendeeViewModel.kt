package com.example.entrypass.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.entrypass.storage.Attendee
import com.example.entrypass.storage.EntryPassDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AttendeeViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = EntryPassDb
        .getInstance(application)
        .attendeeDao()


    suspend fun safeSaveAttendee(attendee: Attendee): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                dao.upsert(attendee)
                true
            } catch (e: Exception) {
                Log.e("EntryPass", "DB insert failed", e)
                false
            }
        }
    }

    suspend fun getAttendeeById(id: Int): Attendee? {
        return withContext(Dispatchers.IO) {
            dao.findById(id)
        }
    }
}

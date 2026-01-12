package com.example.entrypass.screen

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.entrypass.R
import com.example.entrypass.vm.AttendeeViewModel
import kotlinx.coroutines.launch

class CheckinScreen : AppCompatActivity() {

    private lateinit var vm: AttendeeViewModel

    private lateinit var root: LinearLayout
    private lateinit var etLookupId: EditText
    private lateinit var btnVerify: Button
    private lateinit var ivPhoto: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin_screen)

        vm = ViewModelProvider(this)[AttendeeViewModel::class.java]

        root = findViewById(R.id.rootCheckin)
        etLookupId = findViewById(R.id.etLookupId)
        btnVerify = findViewById(R.id.btnVerify)
        ivPhoto = findViewById(R.id.ivResultPhoto)
        tvName = findViewById(R.id.tvResultName)
        tvTitle = findViewById(R.id.tvResultTitle)

        resetUi()

        btnVerify.setOnClickListener {
            val idText = etLookupId.text.toString().trim()
            val id = idText.toIntOrNull()

            if (id == null) {
                Toast.makeText(this, "Enter a valid number.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val attendee = vm.getAttendeeById(id)
                if (attendee == null) {
                    showNotFound()
                } else {
                    showFound(attendee.fullName, attendee.title, attendee.regType, attendee.photoUri)
                }
            }
        }
    }

    private fun resetUi() {
        root.setBackgroundColor(Color.WHITE)
        tvName.text = "Name:"
        tvTitle.text = "Title:"
        ivPhoto.setImageResource(android.R.drawable.ic_menu_gallery)
    }

    private fun showNotFound() {
        root.setBackgroundColor(Color.RED)
        tvName.text = "NOT FOUND"
        tvTitle.text = ""
        ivPhoto.setImageResource(android.R.drawable.ic_delete)
        Toast.makeText(this, "No attendee with that ID.", Toast.LENGTH_SHORT).show()
    }

    private fun showFound(
        name: String,
        title: String,
        regType: String,
        photoUri: String
    ) {
        val bg = when (regType) {
            "Full" -> Color.GREEN
            "Student" -> Color.BLUE
            "None" -> Color.rgb(255, 165, 0)
            else -> Color.LTGRAY
        }

        root.setBackgroundColor(bg)
        tvName.text = "Name: $name"
        tvTitle.text = "Title: $title"

        if (photoUri.isNotBlank()) {
            ivPhoto.setImageURI(Uri.parse(photoUri))
        } else {
            ivPhoto.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }

}

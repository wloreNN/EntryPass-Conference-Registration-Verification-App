package com.example.entrypass.screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.entrypass.R
import com.example.entrypass.storage.Attendee
import com.example.entrypass.vm.AttendeeViewModel
import kotlinx.coroutines.launch
import java.io.File

class SignupScreen : AppCompatActivity() {

    private lateinit var vm: AttendeeViewModel

    private lateinit var etUserId: EditText
    private lateinit var etFullName: EditText
    private lateinit var spTitle: Spinner
    private lateinit var rgType: RadioGroup
    private lateinit var ivPhoto: ImageView
    private lateinit var btnTakePhoto: Button
    private lateinit var btnRegister: Button
    private lateinit var btnGoCheckin: Button
    private lateinit var btnWebsite: Button

    private var photoUri: Uri? = null

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && photoUri != null) {
                ivPhoto.setImageURI(photoUri)
            } else {
                Toast.makeText(this, "Photo capture canceled.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_screen)

        vm = ViewModelProvider(this)[AttendeeViewModel::class.java]

        etUserId = findViewById(R.id.etUserId)
        etFullName = findViewById(R.id.etFullName)
        spTitle = findViewById(R.id.spTitle)
        rgType = findViewById(R.id.rgType)
        ivPhoto = findViewById(R.id.ivPhoto)
        btnTakePhoto = findViewById(R.id.btnTakePhoto)
        btnRegister = findViewById(R.id.btnRegister)
        btnGoCheckin = findViewById(R.id.btnGoCheckin)
        btnWebsite = findViewById(R.id.btnWebsite)

        setupTitleSpinner()


        ivPhoto.setOnClickListener { launchCamera() }
        btnTakePhoto.setOnClickListener { launchCamera() }

        btnRegister.setOnClickListener { registerAttendee() }

        btnGoCheckin.setOnClickListener {
            startActivity(Intent(this, CheckinScreen::class.java))
        }

        btnWebsite.setOnClickListener {
            val url = "https://www.example.com"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    private fun setupTitleSpinner() {
        val titles = listOf("Prof.", "Dr.", "Student")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, titles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTitle.adapter = adapter
    }

    private fun launchCamera() {
        val imgDir = File(cacheDir, "images").apply { mkdirs() }
        val imgFile = File(imgDir, "attendee_${System.currentTimeMillis()}.jpg")

        photoUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.fileprovider",
            imgFile
        )


        val uri = photoUri ?: run {
            Toast.makeText(this, "Could not create file for photo.", Toast.LENGTH_SHORT).show()
            return
        }
        takePicture.launch(uri)
    }

    private fun registerAttendee() {
        val idText = etUserId.text.toString().trim()
        val name = etFullName.text.toString().trim()

        if (idText.isEmpty()) {
            Toast.makeText(this, "User ID is required.", Toast.LENGTH_SHORT).show()
            return
        }
        if (name.isEmpty()) {
            Toast.makeText(this, "Full Name is required.", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = idText.toIntOrNull()
        if (userId == null) {
            Toast.makeText(this, "User ID must be a number.", Toast.LENGTH_SHORT).show()
            return
        }

        val regType = when (rgType.checkedRadioButtonId) {
            R.id.rbFull -> "Full"
            R.id.rbStudent -> "Student"
            R.id.rbNone -> "None"
            else -> ""
        }

        if (regType.isEmpty()) {
            Toast.makeText(this, "Select a registration type.", Toast.LENGTH_SHORT).show()
            return
        }

        val title = spTitle.selectedItem?.toString() ?: "Student"
        val photo = photoUri?.toString().orEmpty()

        val attendee = Attendee(
            userId = userId,
            fullName = name,
            title = title,
            regType = regType,
            photoUri = photo
        )


        lifecycleScope.launch {
            val ok = vm.safeSaveAttendee(attendee)
            if (ok) {
                Toast.makeText(this@SignupScreen, "Registered!", Toast.LENGTH_SHORT).show()
                clearInputs()
            } else {
                Toast.makeText(this@SignupScreen, "Save failed (DB error).", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clearInputs() {
        etUserId.setText("")
        etFullName.setText("")
        rgType.clearCheck()
        photoUri = null
        ivPhoto.setImageResource(android.R.drawable.ic_menu_camera)
    }
}

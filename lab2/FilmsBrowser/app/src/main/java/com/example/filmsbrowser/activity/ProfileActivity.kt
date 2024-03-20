package com.example.filmsbrowser.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.filmsbrowser.databinding.ActivityProfileBinding
import com.example.filmsbrowser.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var binding: ActivityProfileBinding

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.btnToFavored.setOnClickListener {
            val intent = Intent(this@ProfileActivity, FavoredActivity::class.java)
            startActivity(intent)
        }

        binding.btnToFilmsList.setOnClickListener {
            val intent = Intent(this@ProfileActivity, FilmsListActivity::class.java)
            startActivity(intent)
        }

        binding.userImage.setOnClickListener {
            pickImageIntent()
        }

        showData()
    }

    private fun saveImage() {
        val storageRef = storage.getReference("profile_photos/${auth.currentUser!!.uid}")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    Glide
                        .with(this)
                        .load(uri)
                        .centerCrop()
                        .into(binding.userImage)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Loading failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }


    private fun showData() {
        val ref = database.getReference("users").child(auth.currentUser!!.uid)

        ref.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val model = it.result.getValue(User::class.java)

                binding.tvUserLogin.text = String.format("Login: %s", model?.login)
                binding.tvUserEmail.text = String.format("Email: %s", model?.email)
                binding.tvUserName.text = String.format("Name: %s", model?.username)
                binding.tvUserGender.text = String.format("Gender: %s", model?.gender)
                binding.tvUserCountry.text = String.format("Country: %s", model?.country)
                binding.tvUserPatronymic.text = String.format("Patronymic: %s", model?.patronymic)
                binding.tvUserSurname.text = String.format("Surname: %s", model?.surname)
                binding.tvUserInformation.text = String.format("Info: %s", model?.information)
                binding.tvUserBirthDate.text = String.format("Birth date: %s", model?.birthDate)
            } else {
                Toast.makeText(this, "Failed loading profile", Toast.LENGTH_SHORT).show()
            }
        }

        storage.getReference("profile_photos/${auth.currentUser!!.uid}").downloadUrl
            .addOnSuccessListener { uri ->
                Glide
                    .with(this)
                    .load(uri)
                    .centerCrop()
                    .into(binding.userImage)
            }
            .addOnFailureListener() {
                Toast.makeText(this, "Loading failed: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private val imageActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            imageUri = it.data!!.data!!
            saveImage()
        }
    }

    private fun pickImageIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        imageActivityResult.launch(intent)
    }
}
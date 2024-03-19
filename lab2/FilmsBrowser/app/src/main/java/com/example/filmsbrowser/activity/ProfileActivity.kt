package com.example.filmsbrowser.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmsbrowser.databinding.ActivityProfileBinding
import com.example.filmsbrowser.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.btnToFavored.setOnClickListener{
            val intent = Intent(this@ProfileActivity, FavoredActivity::class.java)
            startActivity(intent)
        }

        binding.btnToFilmsList.setOnClickListener{
            val intent = Intent(this@ProfileActivity, FilmsListActivity::class.java)
            startActivity(intent)
        }

        showData()
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
    }
}
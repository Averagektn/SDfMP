package com.example.filmsbrowser.activity

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

        showData()
    }

    private fun showData() {
        val ref = database.getReference("users").child(auth.currentUser!!.uid)

        ref.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val model = it.result.getValue(User::class.java)

                binding.tvUserName.text = model?.login
                binding.tvUserEmail.text = model?.email
            } else {
                Toast.makeText(this, "Failed loading profile", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
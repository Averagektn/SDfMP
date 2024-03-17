package com.example.filmsbrowser

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmsbrowser.databinding.ActivityAuthorizationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthorizationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAuthorizationBinding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val intent = Intent(this@AuthorizationActivity, FilmsListActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Invalid input", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this@AuthorizationActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}
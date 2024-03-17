package com.example.filmsbrowser

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmsbrowser.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegistrationBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.btnSignUp.setOnClickListener {
            val login = binding.etLogin.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (login.isBlank() || login.length < 4) {
                Toast.makeText(applicationContext, "Invalid username", Toast.LENGTH_SHORT).show()
            } else if (email.isBlank()) {
                Toast.makeText(applicationContext, "Invalid email", Toast.LENGTH_SHORT).show()
            } else if (password.isBlank() || password.length < 8) {
                Toast.makeText(applicationContext, "Invalid password", Toast.LENGTH_SHORT).show()
            } else {
                registration(login, email, password)
            }
        }
    }

    private fun registration(login: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { it ->
                if (it.isSuccessful) {
                    val user: FirebaseUser = auth.currentUser!!

                    val usersRef = FirebaseDatabase.getInstance().getReference("users")
                    val newUser = HashMap<String, String>()
                    newUser["login"] = login
                    newUser["uid"] = user.uid
                    newUser["email"] = email

                    usersRef.setValue(newUser).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this@RegistrationActivity, FilmsListActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
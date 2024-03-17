package com.example.filmsbrowser.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmsbrowser.data.User
import com.example.filmsbrowser.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegistrationBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

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

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, AuthorizationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registration(login: String, email: String, password: String) {
        val usersRef = database.getReference("users")
        val query = usersRef.orderByChild("login").equalTo(login)

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful && !task.result.exists()) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        addUser(login, email)
                    } else {
                        Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "This login is already in use", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addUser(login: String, email: String) {
        val users = FirebaseDatabase.getInstance().getReference("users").push()

        val newUser = User(login, email)
        users.setValue(newUser).addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this@RegistrationActivity, FilmsListActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

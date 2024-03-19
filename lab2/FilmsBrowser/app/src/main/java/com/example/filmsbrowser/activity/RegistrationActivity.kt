package com.example.filmsbrowser.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.filmsbrowser.model.User
import com.example.filmsbrowser.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.btnSignUp.setOnClickListener {
            val login = binding.etLogin.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (login.isEmpty() || login.length < 4) {
                Toast.makeText(applicationContext, "Invalid username", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(applicationContext, "Invalid email", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty() || password.length < 8) {
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
        val users = FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser!!.uid)

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

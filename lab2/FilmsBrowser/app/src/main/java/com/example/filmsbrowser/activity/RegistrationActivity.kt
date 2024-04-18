package com.example.filmsbrowser.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.filmsbrowser.databinding.ActivityRegistrationBinding
import com.example.filmsbrowser.viewModel.RegistrationViewModel

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val login = binding.etLogin.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            viewModel.login = login
            viewModel.email = email
            viewModel.password = password

            viewModel.registerUser()
        }

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, AuthorizationActivity::class.java))
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.registrationSuccess.observe(this) { success ->
            if (success) {
                startActivity(Intent(this, FilmsListActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginValidation.observe(this) { isValid ->
            if (!isValid) {
                Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.emailValidation.observe(this) { isValid ->
            if (!isValid) {
                Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.passwordValidation.observe(this) { isValid ->
            if (!isValid) {
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loginInUse.observe(this) { inUse ->
            if (inUse) {
                Toast.makeText(this, "This login is already in use", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

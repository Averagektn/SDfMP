package com.example.filmsbrowser.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.filmsbrowser.databinding.ActivityAuthorizationBinding
import com.example.filmsbrowser.viewModel.AuthorizationViewModel

class AuthorizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthorizationBinding
    private lateinit var viewModel: AuthorizationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthorizationViewModel::class.java]

        binding.btnSignIn.setOnClickListener {
            authorize()
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this@AuthorizationActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }

        observeAuthorizationResult()
    }

    private fun observeAuthorizationResult() {
        viewModel.authorizationResult.observe(this) { success ->
            if (success) {
                val intent = Intent(this@AuthorizationActivity, FilmsListActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Authorization failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun authorize() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        viewModel.authorizeUser(email, password)
    }
}
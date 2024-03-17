package com.example.filmsbrowser.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.filmsbrowser.R
import com.example.filmsbrowser.databinding.ActivityAuthorizationBinding
import com.example.filmsbrowser.databinding.ActivityFilmsListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FilmsListActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFilmsListBinding = ActivityFilmsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }


}
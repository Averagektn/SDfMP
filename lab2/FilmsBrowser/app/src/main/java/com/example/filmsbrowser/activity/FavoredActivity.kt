package com.example.filmsbrowser.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmsbrowser.adapter.FavoredAdapter
import com.example.filmsbrowser.databinding.ActivityFavoredBinding
import com.example.filmsbrowser.model.Film
import com.example.filmsbrowser.viewModel.FavoredViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FavoredActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityFavoredBinding
    private lateinit var favoredAdapter: FavoredAdapter
    private lateinit var favoredViewModel: FavoredViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoredBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.filmsList.layoutManager = LinearLayoutManager(this)

        binding.btnToProfile.setOnClickListener {
            val intent = Intent(this@FavoredActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnToFilmsList.setOnClickListener {
            val intent = Intent(this@FavoredActivity, FilmsListActivity::class.java)
            startActivity(intent)
        }

        favoredViewModel = ViewModelProvider(this)[FavoredViewModel::class.java]

        favoredViewModel.getFavoredFilms(auth.currentUser!!.uid).observe(this) { films ->
            favoredAdapter = FavoredAdapter(this@FavoredActivity, films as ArrayList<Film>)
            binding.filmsList.adapter = favoredAdapter
        }

        favoredViewModel.getErrorMessage().observe(this) { errorMessage ->
            Toast.makeText(this@FavoredActivity, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}

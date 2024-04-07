package com.example.filmsbrowser.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmsbrowser.adapter.FilmListAdapter
import com.example.filmsbrowser.databinding.ActivityFilmsListBinding
import com.example.filmsbrowser.model.Film
import com.example.filmsbrowser.viewModel.FilmsListViewModel

class FilmsListActivity : AppCompatActivity() {
    private lateinit var viewModel: FilmsListViewModel

    private lateinit var binding: ActivityFilmsListBinding
    private lateinit var filmListAdapter: FilmListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FilmsListViewModel::class.java]

        binding.filmsList.layoutManager = LinearLayoutManager(this)

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filmListAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnToFavored.setOnClickListener {
            val intent = Intent(this@FilmsListActivity, FavoredActivity::class.java)
            startActivity(intent)
        }

        binding.btnToProfile.setOnClickListener {
            val intent = Intent(this@FilmsListActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        viewModel.films.observe(this) { films ->
            filmListAdapter = FilmListAdapter(this@FilmsListActivity, films as ArrayList<Film>)
            binding.filmsList.adapter = filmListAdapter
        }

        viewModel.loadFilms()
    }
}
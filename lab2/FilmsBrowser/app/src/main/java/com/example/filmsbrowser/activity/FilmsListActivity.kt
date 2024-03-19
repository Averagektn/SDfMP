package com.example.filmsbrowser.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmsbrowser.adapter.FilmListAdapter
import com.example.filmsbrowser.databinding.ActivityFilmsListBinding
import com.example.filmsbrowser.model.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class FilmsListActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var binding: ActivityFilmsListBinding

    private lateinit var films: ArrayList<Film>
    private lateinit var filmListAdapter: FilmListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

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

        loadFilms()
    }

    private fun loadFilms() {
        films = ArrayList()

        val filmsRef = database.getReference("films")
        filmsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                films.clear()
                for (elem in snapshot.children) {
                    val model = elem.getValue(Film::class.java)

                    if (model != null) {
                        films.add(model)
                    }
                }

                filmListAdapter = FilmListAdapter(this@FilmsListActivity, films)
                binding.filmsList.adapter = filmListAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@FilmsListActivity, "Check your Internet connection", Toast.LENGTH_LONG).show()
            }
        })
    }
}
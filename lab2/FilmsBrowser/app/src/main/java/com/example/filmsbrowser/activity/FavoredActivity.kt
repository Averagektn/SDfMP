package com.example.filmsbrowser.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmsbrowser.adapter.FavoredAdapter
import com.example.filmsbrowser.databinding.ActivityFavoredBinding
import com.example.filmsbrowser.model.Film
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoredActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivityFavoredBinding
    private lateinit var favoredAdapter: FavoredAdapter
    private lateinit var films: ArrayList<Film>

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

        loadFilms()
    }

    private fun loadFilms() {
        films = ArrayList()

        database.getReference("favored/${auth.currentUser!!.uid}").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                loadFavored(snapshot)
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@FavoredActivity, "Check your Internet connection", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadFavored(snapshot: DataSnapshot) {
        val favoredList = ArrayList<String>()

        for (childSnapshot in snapshot.children) {
            val listItem = childSnapshot.getValue(String::class.java)

            if (listItem != null) {
                favoredList.add(listItem)
            }
        }

        val filmsRef = database.getReference("films")
        filmsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                films.clear()

                for (elem in snapshot.children) {
                    if (favoredList.contains(elem.key)) {
                        val model = elem.getValue(Film::class.java)

                        if (model != null) {
                            model.id = elem.key!!
                            films.add(model)
                        }
                    }
                }

                favoredAdapter = FavoredAdapter(this@FavoredActivity, films)
                binding.filmsList.adapter = favoredAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@FavoredActivity, "Check your Internet connection", Toast.LENGTH_LONG).show()
            }
        })
    }
}

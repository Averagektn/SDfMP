package com.example.filmsbrowser.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmsbrowser.model.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FilmsListViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()

    private val _films = MutableLiveData<List<Film>>()
    val films: LiveData<List<Film>> = _films

    fun loadFilms() {
        val filmsRef = database.getReference("films")
        filmsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val films = mutableListOf<Film>()

                for (elem in snapshot.children) {
                    val filmId = elem.key
                    val model = elem.getValue(Film::class.java)

                    if (model != null) {
                        model.id = filmId!!
                        films.add(model)
                    }
                }

                _films.value = films
            }

            override fun onCancelled(p0: DatabaseError) {
                // Handle error
            }
        })
    }
}
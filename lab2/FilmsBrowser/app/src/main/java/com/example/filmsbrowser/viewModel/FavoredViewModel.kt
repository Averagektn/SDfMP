package com.example.filmsbrowser.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmsbrowser.model.Film
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoredViewModel : ViewModel() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val favoredFilms: MutableLiveData<List<Film>> = MutableLiveData()
    private val errorMessage: MutableLiveData<String> = MutableLiveData()

    fun getFavoredFilms(userId: String): LiveData<List<Film>> {
        val favoredRef = database.getReference("favored/$userId")

        favoredRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
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
                        val films = ArrayList<Film>()

                        for (elem in snapshot.children) {
                            if (favoredList.contains(elem.key)) {
                                val model = elem.getValue(Film::class.java)

                                if (model != null) {
                                    model.id = elem.key!!
                                    films.add(model)
                                }
                            }
                        }

                        favoredFilms.value = films
                    }

                    override fun onCancelled(error: DatabaseError) {
                        errorMessage.value = "Check your Internet connection"
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                errorMessage.value = "Check your Internet connection"
            }
        })

        return favoredFilms
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }
}

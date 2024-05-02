package com.example.filmsbrowser.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmsbrowser.model.Comment
import com.example.filmsbrowser.model.Film
import com.example.filmsbrowser.model.User
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FilmViewModel : ViewModel() {
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var commentsRef: DatabaseReference
    private lateinit var favoredFilmsRef: DatabaseReference
    private lateinit var userRef: DatabaseReference

    private val filmLiveData: MutableLiveData<Film?> = MutableLiveData()
    private val favoredFilmsLiveData: MutableLiveData<List<String>> = MutableLiveData()
    private val userLiveData: MutableLiveData<User?> = MutableLiveData()

    fun init(database: FirebaseDatabase, storage: FirebaseStorage) {
        this.database = database
        this.storage = storage
        commentsRef = database.getReference("comments")
        favoredFilmsRef = database.getReference("favored")
        userRef = database.getReference("users")
    }

    fun getFilm(filmId: String): MutableLiveData<Film?> {
        val filmRef = database.getReference("films").child(filmId)
        filmRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val film = snapshot.getValue(Film::class.java)
                filmLiveData.value = film
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return filmLiveData
    }

    fun getCommentsRef(filmId: String): DatabaseReference {
        return commentsRef.child(filmId)
    }

    fun getFavoredFilms(userId: String): LiveData<List<String>> {
        val favoredFilmsQuery = favoredFilmsRef.child(userId)
        favoredFilmsQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val films = mutableListOf<String>()
                for (childSnapshot in snapshot.children) {
                    val filmId = childSnapshot.getValue(String::class.java)
                    filmId?.let {
                        films.add(it)
                    }
                }
                favoredFilmsLiveData.value = films
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return favoredFilmsLiveData
    }

    fun updateFavoredFilms(userId: String, films: List<String>) {
        favoredFilmsRef.child(userId).setValue(films)
    }

    fun getUser(userId: String): MutableLiveData<User?> {
        val userQuery = userRef.child(userId)
        userQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                userLiveData.value = user
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return userLiveData
    }

    fun addComment(filmId: String, comment: Comment) {
        commentsRef.child(filmId).push().setValue(comment)
    }

    fun getFilmImages(storageRef: StorageReference): LiveData<Uri> {
        val uriLiveData: MutableLiveData<Uri> = MutableLiveData()
        storageRef.listAll().addOnSuccessListener { listResult ->
            for (elem in listResult.items) {
                elem.downloadUrl.addOnSuccessListener {
                    uriLiveData.value = it
                }
            }
        }
        return uriLiveData
    }
}
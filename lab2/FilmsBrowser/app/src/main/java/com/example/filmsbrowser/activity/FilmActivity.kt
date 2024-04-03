package com.example.filmsbrowser.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.filmsbrowser.adapter.CommentsAdapter
import com.example.filmsbrowser.adapter.ImageSliderAdapter
import com.example.filmsbrowser.databinding.ActivityFilmBinding
import com.example.filmsbrowser.model.Comment
import com.example.filmsbrowser.model.Film
import com.example.filmsbrowser.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FilmActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var binding: ActivityFilmBinding
    private lateinit var filmId: String

    companion object {
        private val uriList = HashMap<String, ArrayList<Uri>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        filmId = intent.getStringExtra("filmId")!!


        checkIfFilmInFavored()
        initializeSlider()
        initializeFilm()

        val adapter = CommentsAdapter(this, database.getReference("comments/$filmId"))
        val commentList = binding.commentList
        commentList.adapter = adapter

        binding.btnToFilmsList.setOnClickListener {
            val intent = Intent(this, FilmsListActivity::class.java)
            startActivity(intent)
        }

        binding.btnToProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnToFavored.setOnClickListener {
            val intent = Intent(this, FavoredActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddToFavored.setOnClickListener {
            addToFavored()
            binding.btnAddToFavored.visibility = View.GONE
            binding.btnAddToFavored.isEnabled = false
        }

        binding.btnComment.setOnClickListener {
            val uid = auth.currentUser!!.uid
            database.getReference("users/$uid").get().addOnSuccessListener {
                val user = it.getValue(User::class.java)

                database
                    .getReference("comments/$filmId")
                    .push()
                    .setValue(Comment(user!!.login, binding.etComment.text.toString()))
            }
        }
    }

    private fun checkIfFilmInFavored() {
        database.getReference("favored/${auth.currentUser!!.uid}").get().addOnSuccessListener {
            val films = HashSet<String>()

            for (snapshot in it.children) {
                val item = snapshot.getValue(String::class.java)
                if (item != null) {
                    films.add(item)
                }
            }

            if (films.contains(filmId)) {
                binding.btnAddToFavored.visibility = View.GONE
                binding.btnAddToFavored.isEnabled = false
            }
        }
    }

    private fun addToFavored() {
        val ref = database.getReference("favored/${auth.currentUser!!.uid}")

        ref.get().addOnSuccessListener {
            val itemsList = mutableListOf<String>()

            for (childSnapshot in it.children) {
                val listItem = childSnapshot.getValue(String::class.java)
                if (listItem != null) {
                    itemsList.add(listItem)
                }
            }

            itemsList.add(filmId)

            ref.setValue(itemsList)
        }
    }

    private fun initializeFilm() {
        database.getReference("films/$filmId").get().addOnSuccessListener {
            val model = it.getValue(Film::class.java)

            binding.tvFilmName.text = model?.name
            binding.tvCategories.text = model?.categories?.joinToString(", ")
            binding.tvDescription.text = model?.description
        }
    }

    private fun initializeSlider() {
        val storageRef = storage.getReference("film_images/$filmId")

        if (uriList.containsKey(filmId)) {
            val sliderAdapter = ImageSliderAdapter(this, uriList[filmId]!!)
            binding.viewPager.adapter = sliderAdapter
            sliderAdapter.notifyDataSetChanged()
        } else {
            uriList[filmId] = ArrayList()
            storageRef.listAll().addOnSuccessListener { listResult ->
                val sliderAdapter = ImageSliderAdapter(this, uriList[filmId]!!)
                for (elem in listResult.items) {
                    elem.downloadUrl.addOnSuccessListener {
                        uriList[filmId]!!.add(it)
                        sliderAdapter.notifyDataSetChanged()
                    }
                }

                binding.viewPager.adapter = sliderAdapter
            }
        }
    }
}
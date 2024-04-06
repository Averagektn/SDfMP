package com.example.filmsbrowser.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.filmsbrowser.adapter.CommentsAdapter
import com.example.filmsbrowser.adapter.ImageSliderAdapter
import com.example.filmsbrowser.databinding.ActivityFilmBinding
import com.example.filmsbrowser.model.Comment
import com.example.filmsbrowser.viewModel.FilmViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FilmActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val binding = ActivityFilmBinding.inflate(layoutInflater)
    private lateinit var filmId: String
    private lateinit var filmViewModel: FilmViewModel

    companion object {
        private val uriList = HashMap<String, ArrayList<Uri>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        filmId = intent.getStringExtra("filmId")!!

        filmViewModel = ViewModelProvider(this)[FilmViewModel::class.java]
        filmViewModel.init(database, storage)

        checkIfFilmInFavored()
        initializeSlider()
        initializeFilm()

        val adapter = CommentsAdapter(this, filmViewModel.getCommentsRef(filmId))
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
        }

        binding.btnComment.setOnClickListener {
            val uid = auth.currentUser!!.uid
            filmViewModel.getUser(uid).observe(this) { user ->
                user?.let {
                    filmViewModel.addComment(filmId, Comment(user.login, binding.etComment.text.toString()))
                }
            }
        }
    }

    private fun checkIfFilmInFavored() {
        filmViewModel.getFavoredFilms(auth.currentUser!!.uid)?.observe(this) { films ->
            films?.let {
                if (films.contains(filmId)) {
                    binding.btnAddToFavored.visibility = View.GONE
                }
            }
        }
    }

    private fun addToFavored() {
        filmViewModel.getFavoredFilms(auth.currentUser!!.uid)?.observe(this) { films ->
            films?.let {
                val itemsList = films.toMutableList()
                itemsList.add(filmId)
                filmViewModel.updateFavoredFilms(auth.currentUser!!.uid, itemsList)
            }
        }
    }

    private fun initializeFilm() {
        filmViewModel.getFilm(filmId).observe(this) { film ->
            film?.let {
                binding.tvFilmName.text = film.name
                binding.tvCategories.text = film.categories.joinToString(", ")
                binding.tvDescription.text = film.description
            }
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
            filmViewModel.getFilmImages(storageRef)?.observe(this) { uri ->
                uri?.let {
                    uriList[filmId]!!.add(uri)
                    val sliderAdapter = ImageSliderAdapter(this, uriList[filmId]!!)
                    binding.viewPager.adapter = sliderAdapter
                    sliderAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}
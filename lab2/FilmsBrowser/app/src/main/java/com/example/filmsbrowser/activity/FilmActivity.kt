package com.example.filmsbrowser.activity

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.filmsbrowser.adapter.ImageSliderAdapter
import com.example.filmsbrowser.databinding.ActivityFilmBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FilmActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var binding: ActivityFilmBinding

    private lateinit var filmId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        filmId = intent.getStringExtra("filmId")!!

        val storageRef = storage.getReference("film_images/$filmId")
        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                val uriList = ArrayList<Uri>()
                val sliderAdapter = ImageSliderAdapter(this, uriList)
                for(elem in listResult.items){
                    elem.downloadUrl.addOnSuccessListener {
                        uriList.add(it)
                        sliderAdapter.notifyDataSetChanged()
                    }
                }

                binding.viewPager.adapter = sliderAdapter
            }
    }
}
package com.example.filmsbrowser.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.filmsbrowser.activity.FilmActivity
import com.example.filmsbrowser.databinding.ItemFavoredBinding
import com.example.filmsbrowser.model.Film
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FavoredAdapter(private val context: Context, private var films: ArrayList<Film>) :
    RecyclerView.Adapter<FavoredAdapter.FilmHolder>() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ItemFavoredBinding
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    companion object {
        private val imageUrls: HashMap<String, Uri> = HashMap()
    }

    inner class FilmHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = binding.tvFilmName
        var categories = binding.tvFilmCategories
        var image = binding.filmImage
        var delete = binding.btnDelete
    }

    override fun onCreateViewHolder(group: ViewGroup, ind: Int): FilmHolder {
        binding = ItemFavoredBinding.inflate(LayoutInflater.from(context), group, false)

        return FilmHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return films.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: FilmHolder, position: Int) {
        val model = films[position]
        holder.name.text = model.name
        holder.categories.text = model.categories.joinToString(", ")

        holder.image.setImageDrawable(null)

        val imageUrl = imageUrls[model.id]

        if (imageUrl != null) {
            Glide.with(holder.itemView)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .into(holder.image)
        } else {
            val storageRef = storage.getReference("posters/${model.id}")
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                if (position == holder.adapterPosition) {
                    imageUrls[model.id!!] = uri
                    Glide.with(holder.itemView)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerInside()
                        .into(holder.image)
                }
            }
        }

        holder.delete.setOnClickListener {
            val ref = database.getReference("favored/${auth.currentUser!!.uid}")
            ref.get().addOnSuccessListener {
                val itemsList = mutableListOf<String>()

                for (childSnapshot in it.children) {
                    val listItem = childSnapshot.getValue(String::class.java)
                    if (listItem != null) {
                        itemsList.add(listItem)
                    }
                }

                itemsList.remove(model.id)

                ref.setValue(itemsList).addOnSuccessListener {
                    films.remove(model)
                    notifyDataSetChanged()
                }
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FilmActivity::class.java)
            intent.putExtra("filmId", model.id)
            context.startActivity(intent)
        }
    }
}
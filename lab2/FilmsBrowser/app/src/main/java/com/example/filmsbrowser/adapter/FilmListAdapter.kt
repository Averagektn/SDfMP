package com.example.filmsbrowser.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.filmsbrowser.activity.FilmActivity
import com.example.filmsbrowser.databinding.ItemFilmBinding
import com.example.filmsbrowser.filtering.FilterFilm
import com.example.filmsbrowser.model.Film
import com.google.firebase.storage.FirebaseStorage

class FilmListAdapter(private val context: Context, var films: ArrayList<Film>) :
    RecyclerView.Adapter<FilmListAdapter.FilmHolder>(), Filterable {
    private lateinit var binding: ItemFilmBinding
    private var filter: FilterFilm? = null
    private var filterList: ArrayList<Film> = films
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    companion object {
        private val imageUrls: HashMap<String, Uri> = HashMap()
    }

    inner class FilmHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = binding.tvFilmName
        var categories = binding.tvFilmCategories
        var image = binding.filmImage
    }

    override fun onCreateViewHolder(group: ViewGroup, ind: Int): FilmHolder {
        binding = ItemFilmBinding.inflate(LayoutInflater.from(context), group, false)

        return FilmHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return films.size
    }

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

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FilmActivity::class.java)
            intent.putExtra("filmId", model.id)
            context.startActivity(intent)
        }
    }

    /**
     *
     * Returns a filter that can be used to constrain data with a filtering
     * pattern.
     *
     *
     * This method is usually implemented by [android.widget.Adapter]
     * classes.
     *
     * @return a filter used to constrain data
     */
    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterFilm(filterList, this)
        }

        return filter as FilterFilm
    }
}
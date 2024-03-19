package com.example.filmsbrowser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsbrowser.model.Film
import com.example.filmsbrowser.databinding.ItemFilmBinding
import com.example.filmsbrowser.filtering.FilterFilm

class FilmAdapter(private val context: Context, var films: ArrayList<Film>) :
    RecyclerView.Adapter<FilmAdapter.FilmHolder>(), Filterable {
    private lateinit var binding: ItemFilmBinding
    private var filter: FilterFilm? = null
    private var filterList: ArrayList<Film> = films

    inner class FilmHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = binding.tvFilmName
        var categories = binding.tvFilmCategories
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FilmHolder {
        binding = ItemFilmBinding.inflate(LayoutInflater.from(context), p0, false)

        return FilmHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return films.size
    }

    override fun onBindViewHolder(p0: FilmHolder, p1: Int) {
        val model = films[p1]
        val name = model.name
        val categories = model.categories.joinToString(", ")

        p0.name.text = name
        p0.categories.text = categories
        p0.itemView.setOnClickListener {
            Toast.makeText(context, "CLICKED", Toast.LENGTH_LONG).show()
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
        if (filter == null){
            filter = FilterFilm(filterList, this)
        }

        return filter as FilterFilm
    }
}
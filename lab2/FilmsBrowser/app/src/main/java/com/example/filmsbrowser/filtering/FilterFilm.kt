package com.example.filmsbrowser.filtering

import android.widget.Filter
import com.example.filmsbrowser.adapter.FilmListAdapter
import com.example.filmsbrowser.model.Film

class FilterFilm(private var filterList: List<Film>, private var filmListAdapter: FilmListAdapter) : Filter() {
    /**
     *
     * Invoked in a worker thread to filter the data according to the
     * constraint. Subclasses must implement this method to perform the
     * filtering operation. Results computed by the filtering operation
     * must be returned as a [android.widget.Filter.FilterResults] that
     * will then be published in the UI thread through
     * [.publishResults].
     *
     *
     * **Contract:** When the constraint is null, the original
     * data must be restored.
     *
     * @param constraint the constraint used to filter the data
     * @return the results of the filtering operation
     *
     * @see .filter
     * @see .publishResults
     * @see android.widget.Filter.FilterResults
     */
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val results = FilterResults()

        if (!constraint.isNullOrEmpty()) {
            val filteredList = ArrayList<Film>()
            for (film in filterList) {
                if (film.name.contains(constraint, true)) {
                    filteredList.add(film)
                }
            }
            results.values = filteredList
            results.count = filteredList.size
        } else {
            results.values = filterList
            results.count = filterList.size
        }

        return results
    }

    /**
     *
     * Invoked in the UI thread to publish the filtering results in the
     * user interface. Subclasses must implement this method to display the
     * results computed in [.performFiltering].
     *
     * @param constraint the constraint used to filter the data
     * @param results the results of the filtering operation
     *
     * @see .filter
     * @see .performFiltering
     * @see android.widget.Filter.FilterResults
     */
    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        if (results != null) {
            filmListAdapter.films = results.values as ArrayList<Film>
            filmListAdapter.notifyDataSetChanged()
        }
    }
}
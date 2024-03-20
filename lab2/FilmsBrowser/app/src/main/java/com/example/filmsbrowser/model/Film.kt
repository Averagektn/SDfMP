package com.example.filmsbrowser.model

import com.google.firebase.database.Exclude

class Film() {
    @Exclude
    var id: String? = null

    lateinit var name: String
    lateinit var categories: List<String>
    lateinit var description: String

    constructor(name: String, categories: List<String>, description: String) : this() {
        this.name = name
        this.categories = categories
        this.description = description
    }
}

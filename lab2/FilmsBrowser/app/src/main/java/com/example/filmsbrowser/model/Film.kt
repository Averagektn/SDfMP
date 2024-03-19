package com.example.filmsbrowser.model

class Film() {
    lateinit var name: String
    lateinit var categories: List<String>
/*    private lateinit var imageNames: List<String>
    private lateinit var primaryImageName: String*/

    constructor(name: String, categories: List<String>) : this() {
        this.name = name
        this.categories = categories
    }
}

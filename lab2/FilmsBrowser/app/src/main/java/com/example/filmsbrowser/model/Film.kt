package com.example.filmsbrowser.model

class Film() {
    lateinit var name: String
    lateinit var categories: List<String>
    lateinit var id: String
    var description: String = ""

    constructor(name: String, categories: List<String>) : this() {
        this.name = name
        this.categories = categories
    }
}

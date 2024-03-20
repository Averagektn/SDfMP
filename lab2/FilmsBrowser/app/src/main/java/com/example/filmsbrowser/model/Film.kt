package com.example.filmsbrowser.model

class Film() {
    lateinit var name: String
    lateinit var categories: List<String>
    /*    lateinit var imageNames: List<String>
        lateinit var primaryImageName: String
        lateinit var id: String*/

    constructor(name: String, categories: List<String>) : this() {
        this.name = name
        this.categories = categories
    }
}

package com.example.filmsbrowser.model

class Comment() {
    lateinit var author: String
    lateinit var text: String

    constructor(author: String, comment: String) : this() {
        this.author = author
        this.text = comment
    }
}
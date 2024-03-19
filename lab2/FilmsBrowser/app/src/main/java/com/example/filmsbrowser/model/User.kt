package com.example.filmsbrowser.model

class User() {
    lateinit var login: String
    lateinit var email: String

    constructor(login: String, email: String) : this() {
        this.login = login
        this.email = email
    }
}

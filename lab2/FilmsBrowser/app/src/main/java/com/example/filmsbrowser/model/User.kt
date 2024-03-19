package com.example.filmsbrowser.model

class User() {
    lateinit var login: String
    lateinit var email: String
    var birthDate: String = ""
    var information: String = ""
    var username: String = ""
    var surname: String = ""
    var patronymic: String = ""
    var gender: String = ""
    var country: String = ""
    var photoFileName: String = ""

    constructor(login: String, email: String) : this() {
        this.login = login
        this.email = email
    }

    constructor(login: String, email: String, birtDate: String, info: String, username: String, surname: String,
                patronymic: String, gender: String, country: String) : this(login, email){
        this.birthDate = birtDate
        this.information = info
        this.username = username
        this.surname = surname
        this.patronymic = patronymic
        this.gender = gender
        this.country = country
    }
}

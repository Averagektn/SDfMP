import Foundation

class User {
    var login: String
    var email: String
    var information: String
    var username: String
    var surname: String
    var patronymic: String
    var gender: String
    var country: String
    var favoredFilm: String
    var favoredGenre: String

    init(login: String, email: String) {
        self.login = login
        self.email = email
        self.information = ""
        self.username = ""
        self.surname = ""
        self.patronymic = ""
        self.gender = ""
        self.country = ""
        self.favoredFilm = ""
        self.favoredGenre = ""
    }

    init(
        login: String, email: String, information: String, username: String, surname: String,
        patronymic: String, gender: String, country: String, favoredFilm: String, favoredGenre: String
    ) {
        self.login = login
        self.email = email
        self.information = information
        self.username = username
        self.surname = surname
        self.patronymic = patronymic
        self.gender = gender
        self.country = country
        self.favoredFilm = favoredFilm
        self.favoredGenre = favoredGenre
    }
}

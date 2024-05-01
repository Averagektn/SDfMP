class User {
  final String login;
  final String email;
  String information;
  String username;
  String surname;
  String patronymic;
  String gender;
  String country;
  String favoredFilm;
  String favoredGenre;

  User({required this.login, required this.email})
      : information = '',
        username = '',
        surname = '',
        patronymic = '',
        gender = '',
        country = '',
        favoredFilm = '',
        favoredGenre = '';

  User.full({
    required this.login,
    required this.email,
    required this.information,
    required this.username,
    required this.surname,
    required this.patronymic,
    required this.gender,
    required this.country,
    required this.favoredFilm,
    required this.favoredGenre,
  });
}
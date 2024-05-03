namespace FilmsBrowser.Models
{
    public class User
    {
        public string Login { get; set; } = string.Empty;
        public string Email { get; set; } = string.Empty;
        public string Information { get; set; } = string.Empty;
        public string Username { get; set; } = string.Empty;
        public string Surname { get; set; } = string.Empty;
        public string Patronymic { get; set; } = string.Empty;
        public string Gender { get; set; } = string.Empty;
        public string Country { get; set; } = string.Empty;
        public string FavoredFilm { get; set; } = string.Empty;
        public string FavoredGenre { get; set; } = string.Empty;

        public User(string login, string email, string information, string username, string surname, string patronymic,
            string gender, string country, string favoredFilm, string favoredGenre)
        {
            Login = login;
            Email = email;
            Information = information;
            Username = username;
            Surname = surname;
            Patronymic = patronymic;
            Gender = gender;
            Country = country;
            FavoredFilm = favoredFilm;
            FavoredGenre = favoredGenre;
        }
    }
}

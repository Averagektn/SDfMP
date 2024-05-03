using FilmsBrowser.Config;
using FilmsBrowser.Models;
using Firebase.Database;
using Firebase.Database.Query;
using System;
using System.Diagnostics;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace FilmsBrowser.ViewModels
{
    [QueryProperty(nameof(FilmId), nameof(FilmId))]
    public class FilmDetailViewModel : BaseViewModel
    {
        private string filmId;
        private Film film;

        public string FilmId
        {
            get { return filmId; }
            set
            {
                filmId = value;
                LoadFilmId(filmId);
            }
        }

        public Film Film
        {
            get
            {
                return film;
            }
            set
            {
                SetProperty(ref film, value);
            }
        }

        public async void LoadFilmId(string itemId)
        {
            try
            {
                var firebaseClient = new FirebaseClient(MyFirebaseConfig.DatabaseLink, new FirebaseOptions
                {
                    AuthTokenAsyncFactory = () => Task.FromResult(MyFirebaseConfig.WebApiKey)
                });

                Film = await firebaseClient.Child("films").Child(filmId).OnceSingleAsync<Film>();
            }
            catch (Exception)
            {
                Debug.WriteLine("Failed to Load Film");
            }
        }
    }
}

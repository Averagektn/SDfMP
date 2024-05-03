using FilmsBrowser.Cache;
using FilmsBrowser.Config;
using FilmsBrowser.Models;
using Firebase.Database;
using Firebase.Database.Query;
using System;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace FilmsBrowser.ViewModels
{
    [QueryProperty(nameof(FilmId), nameof(FilmId))]
    public class FilmDetailViewModel : BaseViewModel
    {
        private string filmId;
        private Film film;
        public ObservableCollection<Comment> Comments { get; }
        public Command LoadCommentsCommand { get; }

        public string FilmId
        {
            get { return filmId; }
            set
            {
                filmId = value;
                LoadFilmId();
            }
        }

        public Film Film
        {
            get => film;
            set => SetProperty(ref film, value);
        }

        public FilmDetailViewModel()
        {
            Comments = new ObservableCollection<Comment>();
            LoadCommentsCommand = new Command(async () => await ExecuteLoadItemsCommand());
        }

        public async void LoadFilmId()
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

        async Task ExecuteLoadItemsCommand()
        {
            IsBusy = true;

            try
            {
                Comments.Clear();

                var firebaseClient = new FirebaseClient(MyFirebaseConfig.DatabaseLink, new FirebaseOptions
                {
                    AuthTokenAsyncFactory = () => Task.FromResult(MyFirebaseConfig.WebApiKey)
                });
                var filmsRequest = await firebaseClient.Child("comments").Child(filmId).OnceAsync<Comment>();
                var comments = filmsRequest.Select(f => f.Object);

                foreach (var comment in comments)
                {
                    Comments.Add(comment);
                }
            }
            catch (Exception ex)
            {
                Debug.WriteLine(ex);
            }
            finally
            {
                IsBusy = false;
            }
        }

        public void OnAppearing()
        {
            IsBusy = true;
        }
    }
}

using FilmsBrowser.Cache;
using FilmsBrowser.Config;
using FilmsBrowser.Models;
using Firebase.Database;
using Firebase.Database.Query;
using System;
using System.Collections.Generic;
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
        private string _commentText;
        public string CommentText
        {
            get => _commentText;
            set
            {
                _commentText = value;
                OnPropertyChanged(nameof(CommentText));
            }
        }
        private string filmId;
        private Film film;
        public Command LeaveComment { get; }
        public Command AddToFavored { get; }
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
            LeaveComment = new Command(async () => await AddComment());
            AddToFavored = new Command(PerformAddToFavored);
        }

        public async void PerformAddToFavored()
        {
            var d = new Dictionary<string, string>();

            var firebaseClient = new FirebaseClient(MyFirebaseConfig.DatabaseLink, new FirebaseOptions
            {
                AuthTokenAsyncFactory = () => Task.FromResult(MyFirebaseConfig.WebApiKey)
            });
            try
            {
                var favoresRequest = await firebaseClient.Child("favored").Child(FilmCache.Uid).OnceAsync<string>();
                var favores = favoresRequest.Select(f => f.Object).ToList();

                var filmsRequest = await firebaseClient.Child("films").OnceAsync<Film>();
                var films = filmsRequest.Select(f => { f.Object.Id = f.Key; return f.Object; }).ToList();

                foreach (var fav in favores)
                {
                    d.Add(fav, fav);
                }

                if (d.ContainsKey(FilmId))
                {
                    d.Remove(FilmId);
                    FilmCache.FavoredFilms.Remove(FilmId);
                }
                else
                {
                    d.Add(FilmId, FilmId);
                    FilmCache.FavoredFilms.Add(FilmId, films.Where(f => f.Id == FilmId).First());
                }

                await firebaseClient.Child("favored").Child(FilmCache.Uid).DeleteAsync();
                await firebaseClient.Child("favored").Child(FilmCache.Uid).PutAsync(d);
            }
            catch { }
        }

        public async Task AddComment()
        {
            IsBusy = true;

            var firebaseClient = new FirebaseClient(MyFirebaseConfig.DatabaseLink, new FirebaseOptions
            {
                AuthTokenAsyncFactory = () => Task.FromResult(MyFirebaseConfig.WebApiKey)
            });
            var user = await firebaseClient.Child("users").Child(FilmCache.Uid).OnceSingleAsync<User>();
            var comment = new Comment(user.Login, CommentText);

            await firebaseClient.Child("comments").Child(filmId).PostAsync(comment);

            Comments.Add(comment);
            CommentText = string.Empty;
            IsBusy = false;
        }

        public async void LoadFilmId()
        {
            if (FilmCache.Films.ContainsKey(FilmId))
            {
                Film = FilmCache.Films[FilmId];
            }
            else
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

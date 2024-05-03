using FilmsBrowser.Cache;
using FilmsBrowser.Config;
using FilmsBrowser.Models;
using FilmsBrowser.Views;
using Firebase.Database;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Threading.Tasks;
using System;
using Xamarin.Forms;
using System.Linq;
using System.Collections.Generic;
using Firebase.Database.Query;
using System.Collections.Immutable;

namespace FilmsBrowser.ViewModels
{
    public class FavoredViewModel : BaseViewModel
    {
        private Film _selectedItem;
        public ObservableCollection<Film> Films { get; }
        public Command LoadFilmsCommand { get; }
        public Command<Film> FilmTapped { get; }
        public string SearchText { get; set; }

        public FavoredViewModel()
        {
            Title = "Films";
            Films = new ObservableCollection<Film>();
            LoadFilmsCommand = new Command(async () => await ExecuteLoadItemsCommand());

            FilmTapped = new Command<Film>(OnItemSelected);
        }

        public void SearchFilm()
        {
            Films.Clear();
            var found = FilmCache.FavoredFilms.Values.Where(f => f.Name.ToLower().Contains(SearchText.ToLower()));

            foreach (var film in found)
            {
                Films.Add(film);
            }
        }

        async Task ExecuteLoadItemsCommand()
        {
            IsBusy = true;

            try
            {
                Films.Clear();

                if (FilmCache.FavoredFilms.Count != 0)
                {
                    foreach (var film in FilmCache.FavoredFilms.Values)
                    {
                        Films.Add(film);
                    }
                }
                else
                {
                    var firebaseClient = new FirebaseClient(MyFirebaseConfig.DatabaseLink, new FirebaseOptions
                    {
                        AuthTokenAsyncFactory = () => Task.FromResult(MyFirebaseConfig.WebApiKey)
                    });
                    var filmsRequest = await firebaseClient.Child("films").OnceAsync<Film>();
                    var films = filmsRequest.Select(f =>
                    {
                        f.Object.Id = f.Key;
                        return f.Object;
                    });

                    var favoredRequest = await firebaseClient.Child("favored").Child(MyFirebaseConfig.Uid).OnceAsync<string>();
                    var favored = favoredRequest.Select(f => f.Object).ToImmutableHashSet();

                    foreach (var film in films)
                    {
                        if (favored.Contains(film.Id))
                        {
                            FilmCache.FavoredFilms.Add(film.Id, film);
                            Films.Add(film);
                        }
                    }
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
            SelectedItem = null;
        }

        public Film SelectedItem
        {
            get => _selectedItem;
            set
            {
                SetProperty(ref _selectedItem, value);
                OnItemSelected(value);
            }
        }

        async void OnItemSelected(Film film)
        {
            if (film is null)
            {
                return;
            }

            await Shell.Current.GoToAsync($"{nameof(FilmDetailPage)}?{nameof(FilmDetailViewModel.FilmId)}={film.Id}");
        }
    }
}

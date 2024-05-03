using FilmsBrowser.Cache;
using FilmsBrowser.Config;
using FilmsBrowser.Models;
using FilmsBrowser.Views;
using Firebase.Database;
using System;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace FilmsBrowser.ViewModels
{
    public class FilmsViewModel : BaseViewModel
    {
        private Film _selectedItem;
        public ObservableCollection<Film> Films { get; }
        public Command LoadFilmsCommand { get; }
        public Command<Film> FilmTapped { get; }
        public string SearchText { get; set; }

        public FilmsViewModel()
        {
            Title = "Films";
            Films = new ObservableCollection<Film>();
            LoadFilmsCommand = new Command(async () => await ExecuteLoadItemsCommand());

            FilmTapped = new Command<Film>(OnItemSelected);
        }

        public void SearchFilm()
        {
            Films.Clear();
            var found = FilmCache.Films.Values.Where(f => f.Name.ToLower().Contains(SearchText.ToLower()));

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

                if (FilmCache.Films.Count != 0)
                {
                    foreach (var film in FilmCache.Films.Values)
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

                    foreach (var film in films)
                    {
                        FilmCache.Films.Add(film.Id, film);
                        Films.Add(film);
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
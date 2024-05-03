using FilmsBrowser.Cache;
using FilmsBrowser.Config;
using FilmsBrowser.Models;
using FilmsBrowser.Views;
using Firebase.Database;
using Firebase.Storage;
using System;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Linq;
using System.Net.Http;
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

        public FilmsViewModel()
        {
            Title = "Films";
            Films = new ObservableCollection<Film>();
            LoadFilmsCommand = new Command(async () => await ExecuteLoadItemsCommand());

            FilmTapped = new Command<Film>(OnItemSelected);
        }

        async Task ExecuteLoadItemsCommand()
        {
            IsBusy = true;

            try
            {
                Films.Clear();

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

                var storage = new FirebaseStorage("films-browser-8b64a.appspot.com", new FirebaseStorageOptions
                {
                    AuthTokenAsyncFactory = () => Task.FromResult(MyFirebaseConfig.WebApiKey)
                });
                using (var client = new HttpClient())
                {
                    foreach (var film in films)
                    {
                        if (FilmCache.Films.ContainsKey(film.Id))
                        {
                            Films.Add(FilmCache.Films[film.Id]);
                        }
                        else
                        {
                            var downloadUrl = await storage
                                .Child("posters")
                                .Child(film.Id)
                                .GetDownloadUrlAsync();

                            var response = await client.GetAsync(downloadUrl);
                            if (response.IsSuccessStatusCode)
                            {
                                var stream = await response.Content.ReadAsStreamAsync();
                                film.Source = ImageSource.FromStream(() => stream);
                            }
                            FilmCache.Films.Add(film.Id, film);
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
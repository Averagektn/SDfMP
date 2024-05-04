using FilmsBrowser.Cache;
using FilmsBrowser.Config;
using FilmsBrowser.ViewModels;
using Firebase.Storage;
using System.Collections.ObjectModel;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace FilmsBrowser.Views
{
    public partial class FilmDetailPage : ContentPage
    {
        readonly FilmDetailViewModel _viewModel;

        public FilmDetailPage()
        {
            InitializeComponent();
            BindingContext = _viewModel = new FilmDetailViewModel();
        }

        protected override async void OnAppearing()
        {
            base.OnAppearing();
            _viewModel.OnAppearing();

            var storage = new FirebaseStorage(MyFirebaseConfig.StorageLink, new FirebaseStorageOptions
            {
                AuthTokenAsyncFactory = () => Task.FromResult(MyFirebaseConfig.WebApiKey)
            });

            var collection = new ObservableCollection<Image>();
            try
            {

                var downloadUrl = await storage
                    .Child("film_images")
                    .Child(_viewModel.FilmId)
                    .Child(_viewModel.FilmId)
                    .GetDownloadUrlAsync();
                collection.Add(new Image() { Source = downloadUrl });
            }
            catch { }

            try
            {
                if (FilmCache.StorageLinks.ContainsKey(_viewModel.FilmId))
                {
                    foreach (var link in FilmCache.StorageLinks[_viewModel.FilmId])
                    {
                        var downloadUrl = await storage
                            .Child("film_images")
                            .Child(_viewModel.FilmId)
                            .Child(link)
                            .GetDownloadUrlAsync();
                        collection.Add(new Image() { Source = downloadUrl });
                    }
                }
            }
            catch { }

            Carousel.ItemsSource = collection;
        }
    }
}
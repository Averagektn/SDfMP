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

            try
            {
                var downloadUrl = await storage
                    .Child("film_images")
                    .Child(_viewModel.FilmId)
                    .Child(_viewModel.FilmId)
                    .GetDownloadUrlAsync();

                Carousel.ItemsSource = new ObservableCollection<Image>()
                {
                    new Image() { Source = downloadUrl }
                };
            }
            catch { }
        }
    }
}
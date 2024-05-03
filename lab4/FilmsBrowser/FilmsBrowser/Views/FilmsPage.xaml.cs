using FilmsBrowser.ViewModels;
using Xamarin.Forms;

namespace FilmsBrowser.Views
{
    public partial class FilmsPage : ContentPage
    {
        readonly FilmsViewModel _viewModel;

        public FilmsPage()
        {
            InitializeComponent();
            BindingContext = _viewModel = new FilmsViewModel();
        }

        protected override void OnAppearing()
        {
            base.OnAppearing();
            _viewModel.OnAppearing();
        }

        private void Entry_TextChanged(object sender, TextChangedEventArgs e)
        {
            _viewModel.SearchFilm();
        }
    }
}
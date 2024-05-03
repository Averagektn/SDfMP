using FilmsBrowser.ViewModels;
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

        protected override void OnAppearing()
        {
            base.OnAppearing();
            _viewModel.OnAppearing();
        }
    }
}
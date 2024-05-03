using FilmsBrowser.ViewModels;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace FilmsBrowser.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class FavoredPage : ContentPage
    {
        readonly FavoredViewModel _viewModel;

        public FavoredPage()
        {
            InitializeComponent();
            BindingContext = _viewModel = new FavoredViewModel();
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
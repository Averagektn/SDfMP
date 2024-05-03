using FilmsBrowser.ViewModels;
using Xamarin.Forms;

namespace FilmsBrowser.Views
{
    public partial class ProfilePage : ContentPage
    {
        readonly ProfileViewModel _viewModel;

        public ProfilePage()
        {
            InitializeComponent();
            BindingContext = _viewModel = new ProfileViewModel();
        }

        protected override void OnAppearing()
        {
            base.OnAppearing();
            _viewModel.OnAppearing();
        }
    }
}
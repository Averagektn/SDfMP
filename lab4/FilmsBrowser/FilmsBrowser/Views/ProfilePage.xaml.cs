using FilmsBrowser.ViewModels;
using Xamarin.Forms;

namespace FilmsBrowser.Views
{
    public partial class ProfilePage : ContentPage
    {
        public ProfilePage()
        {
            InitializeComponent();
            BindingContext = new ProfileViewModel();
        }
    }
}
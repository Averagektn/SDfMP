using FilmsBrowser.ViewModels;
using Xamarin.Forms;

namespace FilmsBrowser.Views
{
    public partial class FilmDetailPage : ContentPage
    {
        public FilmDetailPage()
        {
            InitializeComponent();
            BindingContext = new FilmDetailViewModel();
        }
    }
}
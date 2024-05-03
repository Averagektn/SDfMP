using FilmsBrowser.ViewModels;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace FilmsBrowser.Views
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class RegistrationPage : ContentPage
    {
        public RegistrationPage()
        {
            InitializeComponent();
            BindingContext = new RegistrationViewModel();
        }
    }
}
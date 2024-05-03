using FilmsBrowser.Models;
using System.Windows.Input;
using Xamarin.Essentials;
using Xamarin.Forms;

namespace FilmsBrowser.ViewModels
{
    public class ProfileViewModel : BaseViewModel
    {
        public User User { get; set; }

        public ProfileViewModel()
        {
            Title = "About";
        }
    }
}
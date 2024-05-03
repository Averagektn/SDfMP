using FilmsBrowser.Models;
using Xamarin.Forms;

namespace FilmsBrowser.ViewModels
{
    public class ProfileViewModel : BaseViewModel
    {
        public Command Update { get; }
        public Command Delete { get; }
        public Command Logout { get; }
        public User User { get; set; }

        public ProfileViewModel()
        {
            Title = "Profile";

            Update = new Command(PerformUpdate);
            Delete = new Command(DeleteProfile);
            Logout = new Command(PerformLogout);
        }

        public void OnAppearing()
        {

        }

        public void PerformLogout()
        {

        }

        public void DeleteProfile()
        {

        }

        public void PerformUpdate()
        {

        }
    }
}
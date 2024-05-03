using FilmsBrowser.Config;
using FilmsBrowser.Views;
using Firebase.Auth;
using Xamarin.Forms;

namespace FilmsBrowser.ViewModels
{
    public class LoginViewModel : BaseViewModel
    {
        public string Email { get; set; }
        public string Password { get; set; }
        public Command LoginCommand { get; }

        public LoginViewModel()
        {
            LoginCommand = new Command(OnLoginClicked);
        }

        private async void OnLoginClicked(object obj)
        {
            try
            {
                var authProvider = new FirebaseAuthProvider(new FirebaseConfig(MyFirebaseConfig.WebApiKey));
                var authResult = await authProvider.SignInWithEmailAndPasswordAsync(Email, Password);
                var user = authResult.User;
            }
            catch
            {
                await Application.Current.MainPage.DisplayAlert("Fail", "Invalid login or password", "OK");
            }

            await Shell.Current.GoToAsync($"//{nameof(FilmsPage)}");
        }
    }
}

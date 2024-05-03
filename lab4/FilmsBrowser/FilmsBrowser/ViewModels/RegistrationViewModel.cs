using FilmsBrowser.Config;
using FilmsBrowser.Views;
using Firebase.Auth;
using Firebase.Database;
using Firebase.Database.Query;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace FilmsBrowser.ViewModels
{
    internal class RegistrationViewModel : BaseViewModel
    {
        public string Login { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public Command RegistrationCommand { get; }

        public RegistrationViewModel()
        {
            RegistrationCommand = new Command(OnRegistrationClicked);
        }

        private async void OnRegistrationClicked(object obj)
        {
            if (Login.Length < 4)
            {
                await Application.Current.MainPage.DisplayAlert("Fail", "Login is too short", "OK");
                return;
            }

            if (Password.Length < 8)
            {
                await Application.Current.MainPage.DisplayAlert("Fail", "Password is too short", "OK");
                return;
            }

            try
            {
                var authProvider = new FirebaseAuthProvider(new FirebaseConfig(MyFirebaseConfig.WebApiKey));
                var authResult = await authProvider.CreateUserWithEmailAndPasswordAsync(Email, Password);
                MyFirebaseConfig.Uid = authResult.User.LocalId;
                MyFirebaseConfig.FirebaseToken = authResult.FirebaseToken;

                var firebaseClient = new FirebaseClient(MyFirebaseConfig.DatabaseLink, new FirebaseOptions
                {
                    AuthTokenAsyncFactory = () => Task.FromResult(MyFirebaseConfig.WebApiKey)
                });
                await firebaseClient.Child("users").Child(MyFirebaseConfig.Uid).PutAsync(new Models.User()
                {
                    Email = this.Email,
                    Login = this.Login
                });

                await Shell.Current.GoToAsync($"//{nameof(FilmsPage)}");
            }
            catch
            {
                await Application.Current.MainPage.DisplayAlert("Fail", "Incorrect credencials", "OK");
            }
        }
    }
}

using FilmsBrowser.Cache;
using FilmsBrowser.Config;
using FilmsBrowser.Views;
using Firebase.Auth;
using Firebase.Database;
using Firebase.Database.Query;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace FilmsBrowser.ViewModels
{
    public class ProfileViewModel : BaseViewModel
    {
        public Command Update { get; }
        public Command Delete { get; }
        public Command Logout { get; }
        private Models.User _user;

        public Models.User User
        {
            get => _user;
            set
            {
                _user = value;
                OnPropertyChanged(nameof(User));
            }
        }

        public ProfileViewModel()
        {
            Title = "Profile";

            Update = new Command(PerformUpdate);
            Delete = new Command(DeleteProfile);
            Logout = new Command(PerformLogout);
        }

        public async void OnAppearing()
        {
            var firebaseClient = new FirebaseClient(MyFirebaseConfig.DatabaseLink, new FirebaseOptions
            {
                AuthTokenAsyncFactory = () => Task.FromResult(MyFirebaseConfig.WebApiKey)
            });
            User = await firebaseClient.Child("users").Child(FilmCache.Uid).OnceSingleAsync<Models.User>();
        }

        public async void PerformLogout()
        {
            FilmCache.Uid = string.Empty;
            await Shell.Current.GoToAsync($"//{nameof(LoginPage)}");
        }

        public async void DeleteProfile()
        {
            var authProvider = new FirebaseAuthProvider(new FirebaseConfig(MyFirebaseConfig.WebApiKey));
            await authProvider.DeleteUserAsync(FilmCache.FirebaseToken);
            FilmCache.FirebaseToken = string.Empty;
            await Shell.Current.GoToAsync($"//{nameof(LoginPage)}");
        }

        public async void PerformUpdate()
        {
            var firebaseClient = new FirebaseClient(MyFirebaseConfig.DatabaseLink, new FirebaseOptions
            {
                AuthTokenAsyncFactory = () => Task.FromResult(MyFirebaseConfig.WebApiKey)
            });
            await firebaseClient.Child("users").Child(FilmCache.Uid).PutAsync(User);
        }
    }
}
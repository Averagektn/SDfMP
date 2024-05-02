using FilmsBrowser.ViewModels;
using System.ComponentModel;
using Xamarin.Forms;

namespace FilmsBrowser.Views
{
    public partial class ItemDetailPage : ContentPage
    {
        public ItemDetailPage()
        {
            InitializeComponent();
            BindingContext = new ItemDetailViewModel();
        }
    }
}
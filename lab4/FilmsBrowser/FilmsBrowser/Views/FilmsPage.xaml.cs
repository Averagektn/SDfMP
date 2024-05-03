using FilmsBrowser.Models;
using FilmsBrowser.ViewModels;
using FilmsBrowser.Views;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace FilmsBrowser.Views
{
    public partial class FilmsPage : ContentPage
    {
        FilmsViewModel _viewModel;

        public FilmsPage()
        {
            InitializeComponent();

            BindingContext = _viewModel = new FilmsViewModel();
        }

        protected override void OnAppearing()
        {
            base.OnAppearing();
            _viewModel.OnAppearing();
        }
    }
}
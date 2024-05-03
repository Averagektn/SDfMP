using FilmsBrowser.ViewModels;
using FilmsBrowser.Views;
using System;
using System.Collections.Generic;
using Xamarin.Forms;

namespace FilmsBrowser
{
    public partial class AppShell : Xamarin.Forms.Shell
    {
        public AppShell()
        {
            InitializeComponent();

            Routing.RegisterRoute(nameof(FilmDetailPage), typeof(FilmDetailPage));
        }
    }
}

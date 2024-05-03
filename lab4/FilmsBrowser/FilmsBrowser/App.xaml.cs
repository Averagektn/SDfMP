﻿using FilmsBrowser.Services;
using FilmsBrowser.Views;
using System;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace FilmsBrowser
{
    public partial class App : Application
    {

        public App()
        {
            InitializeComponent();

            DependencyService.Register<MockDataStore>();
            //MainPage = new AppShell();
            MainPage = new RegistrationPage();
        }

        protected override void OnStart()
        {
        }

        protected override void OnSleep()
        {
        }

        protected override void OnResume()
        {
        }
    }
}

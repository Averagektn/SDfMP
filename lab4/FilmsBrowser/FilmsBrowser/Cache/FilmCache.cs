using FilmsBrowser.Models;
using System.Collections.Generic;
using Xamarin.Essentials;

namespace FilmsBrowser.Cache
{
    public static class FilmCache
    {
        public static Dictionary<string, Film> Films = new Dictionary<string, Film>();
        public static Dictionary<string, Film> FavoredFilms = new Dictionary<string, Film>();

        public static readonly Dictionary<string, List<string>> StorageLinks = new Dictionary<string, List<string>>()
        {
            {
                "-NtSNsLQQe3ET8K7E2Di", 
                new List<string>() 
                { 
                    "1611978.webp", 
                    "unnamed.webp", 
                    "Captain.jpg", 
                    "Catpain1.jpg", 
                    "Catpain2.jpg", 
                    "81UBDps8YpL._AC_UF1000,1000_QL80_.jpg", 
                    "The-Captain.jpg" 
                } 
            },
            {
                "-NtSNsLWaHZDxdBOkIEM", 
                new List<string>() 
                {
                    "123Untitled.jpg",
                    "43Untitled.jpg",
                    "MV5BMTg2NDg3ODg4NF5BMl5BanBnXkFtZTcwNzk3NTc3Nw@@._V1_.jpg",
                    "5903ff76e39f71633fb337e643111cc8.jpg",
                    "Untitled.jpg",
                    "mitchell-netes-144.jpg",
                    "the-hunt.jpg"
                } 
            },
            {
                "-NtSNsLX9gs0GZxlsHZu", 
                new List<string>() 
                {
                    "killers-of-the-flower-moon-9781398513341_hr.jpg",
                    "Untitled.jpg",
                    "MV5BMTdiOTg2YmQtZTdjNi00NGJjLWI2ZTQtYWNkNDUwMDEzOTQxXkEyXkFqcGdeQXVyMTAxNzQ1NzI@._V1_FMjpg_UX1000_.jpg",
                    "61MhIlXWJbL._AC_UF1000,1000_QL80_.jpg",
                    "57Untitled.jpg",
                    "21killers-cameos-articleLarge.webp",
                    "Killers_Of_The_Flower_Moon_Photo_0108.jpg"
                } 
            },
            {
                "-NtSNsLY8LkJMr-kd7WF", 
                new List<string>() 
                {
                    "12554Untitled.jpg",
                    "78Untitled.jpg",
                    "Untitled.jpg",
                    "MV5BZmE0MGJhNmYtOWNjYi00Njc5LWE2YjEtMWMxZTVmODUwMmMxXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg",
                    "images.jpg",
                    "original.avif",
                    "the-lighthouse-movie-poster.jpg\r\n"
                } 
            },
            {
                "-NtSNsLZFIk-tMn1mQQd", 
                new List<string>() 
                {
                    "Untitled.jpg",
                    "Oppenheimer_(film).jpg.webp",
                    "MV5BMWE4OTM1ZjItYzc5Ni00MDFhLWFiZWEtOWM1ODlkZjQyNzM0XkEyXkFqcGdeQWthc2hpa2F4._V1_QL75_UY281_CR0,0,500,281_.jpg",
                    "Brody-Op-Review.webp",
                    "678Untitled.jpg",
                    "6544Untitled.jpg",
                    "56789images.jpg"
                } 
            }
        };
    }
}

using FilmsBrowser.Models;
using System.Collections.Generic;

namespace FilmsBrowser.Cache
{
    public static class FilmCache
    {
        public static Dictionary<string, Film> Films = new Dictionary<string, Film>();
        public static Dictionary<string, Film> FavoredFilms = new Dictionary<string, Film>();
    }
}

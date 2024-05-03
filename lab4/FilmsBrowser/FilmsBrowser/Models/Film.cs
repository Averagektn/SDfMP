using System.IO;
using System.Linq;
using Xamarin.Forms;

namespace FilmsBrowser.Models
{
    public class Film
    {
        public string Id { get; set; } = string.Empty;
        public string Name { get; set; }
        public string[] Categories { get; set; }
        public string CategoriesString => string.Join(" ", Categories);
        public string Description { get; set; }
        public ImageSource Source { get; set; }

        public Film() : this(string.Empty, new string[1] { string.Empty }, string.Empty)
        {

        }

        public Film(string name, string[] categories, string description)
        {
            Name = name;
            Categories = categories;
            Description = description;
        }
    }
}

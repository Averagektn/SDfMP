import SwiftUI
import FirebaseDatabase

class FilmsListViewModel: ObservableObject {
    @Published var films: [Film] = []
    
    private let database = Database.database()
    
    func loadFilms() {
        let filmsRef = database.reference().child("films")
        filmsRef.observe(.value) { snapshot in
            var films: [Film] = []
            
            for child in snapshot.children {
                if let snapshot = child as? DataSnapshot,
                   let filmId = snapshot.key as String?,
                   let filmValue = snapshot.value as? [String: Any] {
                    var film = Film(name: filmValue["name"] as? String ?? "",
                                    categories: filmValue["categories"] as? [String] ?? [],
                                    description: filmValue["description"] as? String ?? "")
                    film.id = filmId
                    films.append(film)
                }
            }
            
            self.films = films
        }
    }
}

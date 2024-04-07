import Foundation
import FirebaseDatabase
import FirebaseAuth

class FavoredViewModel: ObservableObject {
    private let database = Database.database()
    private let auth = Auth.auth()
    
    @Published var films: [Film] = []
    
    func loadFilms() {
        guard let uid = auth.currentUser?.uid else {
            return
        }
        
        let favoredRef = database.reference().child("favored").child(uid)
        favoredRef.observe(.value) { [weak self] snapshot in
            var favoredFilmIds: [String] = []
            
            for child in snapshot.children {
                if let snapshot = child as? DataSnapshot,
                   let filmId = snapshot.value as? String {
                    favoredFilmIds.append(filmId)
                }
            }
            
            let filmsRef = self?.database.reference().child("films")
            filmsRef?.observe(.value) { [favoredFilmIds] snapshot in
                var films: [Film] = []
                
                for child in snapshot.children {
                    if let snapshot = child as? DataSnapshot,
                       let filmId = snapshot.key as String?,
                       let filmValue = snapshot.value as? [String: Any],
                       favoredFilmIds.contains(filmId) {
                        let film = Film(name: filmValue["name"] as? String ?? "",
                                        categories: filmValue["categories"] as? [String] ?? [],
                                        description: filmValue["description"] as? String ?? "")
                        film.id = filmId
                        films.append(film)
                    }
                }
                
                self?.films = films
            }
        }
    }
}

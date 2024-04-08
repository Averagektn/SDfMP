import SwiftUI
import FirebaseStorage
import FirebaseDatabase
import FirebaseAuth

class FilmViewModel: ObservableObject {
    static var cache = NSCache<NSString, UIImage>()
    static var cachedSlider = NSCache<NSString, NSArray>()
    
    var film: Film
    
    private let uid = Auth.auth().currentUser?.uid
    private var storage = Storage.storage()
    private var database = Database.database()
    
    @Published var images: [UIImage] = []
    @Published var comments: [Comment] = []
    @Published var newComment = ""
    
    init(film: Film){
        self.film = film
    }
    
    func removeFromFavored(){
        let favoredRef = self.database.reference().child("favored").child(uid!)
        
        favoredRef.observeSingleEvent(of: .value) { snapshot in
            if snapshot.exists() {
                if var data = snapshot.value as? [String: Any] {
                    data = data.filter { $0.value as? String != self.film.id! }
                    favoredRef.setValue(data)
                }
            }
        }
    }
    
    func checkIfFilmIdExists(completion: @escaping (Bool) -> Void) {
        let favoredRef = database.reference().child("favored").child(uid!)
        
        favoredRef.observeSingleEvent(of: .value) { [self] snapshot in
            if let data = snapshot.value as? [String: Any] {
                for value in data.values {
                    if let filmId = value as? String, filmId == film.id! {
                        completion(true)
                        return
                    }
                }
            }
            completion(false)
        }
    }
    
    func addToFavored(){
        let favoredRef = self.database.reference().child("favored").child(uid!)
        favoredRef.childByAutoId().setValue(film.id)
    }
    
    func addComment() {
        database.reference().child("users").child(uid!).child("login").observeSingleEvent(of: .value) { [self] snapshot in
            
            let comment = Comment(author: snapshot.value as! String, comment: self.newComment)
            let commentsRef = self.database.reference().child("comments").child(film.id!)
            
            let commentDictionary: [String: Any] = [
                "author": comment.author,
                "text": comment.text
            ]
            
            commentsRef.childByAutoId().setValue(commentDictionary)
            comments.append(comment)
        }
    }
    
    func getSlider(){
        let sliderRef = storage.reference().child("film_images").child(film.id!)
        
        let cacheKey = film.id! as NSString
        
        sliderRef.listAll { [self] (result, error) in
            if error != nil {
                return
            }
            self.images = []
            
            for file in result!.items {
                file.getData(maxSize: Int64.max) { [self] (data, error) in
                    if error != nil {
                        return
                    }
                    
                    if let data = data, let image = UIImage(data: data) {
                        images.append(image)
                    }
                }
            }
            
            FilmViewModel.cachedSlider.setObject(images as NSArray, forKey: cacheKey)
        }
    }
    
    func getComments(){
        let commentsRef = database.reference().child("comments").child(film.id!)
        
        commentsRef.observeSingleEvent(of: .value) { snapshot  in
            guard snapshot.exists(), let commentsData = snapshot.value as? [String: Any] else {
                return
            }
            
            for commentData in commentsData {
                if let commentDict = commentData.value as? [String: String],
                   let author = commentDict["author"],
                   let text = commentDict["text"] {
                    let comment = Comment(author: author, comment: text)
                    self.comments.append(comment)
                }
            }
        }
    }
    
    func getImage(completion: @escaping (UIImage?) -> Void) {
        if let cachedImage = FilmViewModel.cache.object(forKey: self.film.id! as NSString) {
            completion(cachedImage)
        } else {
            DispatchQueue.global().async {
                while FilmViewModel.cache.object(forKey: self.film.id! as NSString) == nil {
                    usleep(10000)
                }
                
                if let image = FilmViewModel.cache.object(forKey: self.film.id! as NSString) {
                    completion(image)
                } else {
                    completion(nil)
                }
            }
        }
    }
    
    func loadImage() {
        if FilmViewModel.cache.object(forKey: film.id! as NSString) == nil {
            let posterRef = storage.reference().child("posters").child(film.id!)
            posterRef.downloadURL { url, error in
                if let imageURL = url {
                    URLSession.shared.dataTask(with: imageURL) { data, _, _ in
                        if let data = data, let image = UIImage(data: data) {
                            FilmViewModel.cache.setObject(image, forKey: self.film.id! as NSString)
                        }
                    }.resume()
                }
            }
        }
    }
}

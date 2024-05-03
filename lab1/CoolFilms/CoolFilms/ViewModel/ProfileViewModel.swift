import Foundation
import FirebaseDatabase
import FirebaseAuth
import SwiftUI
import FirebaseStorage

class ProfileViewModel: ObservableObject {
    @Published var user = User(login: "empty", email: "empty", information: "empty", username: "empty", surname: "empty", patronymic: "empty", gender: "empty", country: "empty", favoredFilm: "empty", favoredGenre: "empty")
    
    private let uid = Auth.auth().currentUser?.uid
    private let storage = Storage.storage()
    private let database = Database.database().reference()
    
    private static var cache = NSCache<NSString, UIImage>()
    
    func loadData() {
        let database = Database.database().reference()
        
        database.child("users").child(uid!).observeSingleEvent(of: .value) { (snapshot) in
            if let value = snapshot.value as? [String: Any] {
                self.user.email = value["email"] as? String ?? "empty"
                self.user.login = value["login"] as? String ?? "empty"
                self.user.information = value["information"] as? String ?? "empty"
                self.user.username = value["username"] as? String ?? "empty"
                self.user.surname = value["surname"] as? String ?? "empty"
                self.user.patronymic = value["patronymic"] as? String ?? "empty"
                self.user.gender = value["gender"] as? String ?? "empty"
                self.user.country = value["country"] as? String ?? "empty"
                self.user.favoredFilm = value["favoredFilm"] as? String ?? "empty"
                self.user.favoredGenre = value["favoredGenre"] as? String ?? "empty"
                
                self.objectWillChange.send()
            }
        }
    }
    
    func logOut(){
        do {
            try Auth.auth().signOut()
        } catch let signOutError as NSError {
            print("ERROR")
        }
    }
    
    func deleteProfile(){
        database.child("users").child(uid!).removeValue()
        database.child("favored").child(uid!).removeValue()
        Auth.auth().currentUser?.delete()
    }
    
    func update() {
        let ref = Database.database().reference().child("users").child(uid!)
        let userDictionary: [String: Any] = [
            "login": user.login,
            "country": user.country,
            "gender": user.gender,
            "patronymic": user.patronymic,
            "surname": user.surname,
            "name": user.username,
            "email": user.email,
            "info": user.information,
            "favoredFilm": user.favoredFilm,
            "favoredGenre": user.favoredGenre
        ]
        ref.setValue(userDictionary)
        
        self.objectWillChange.send()
    }
}

import Foundation
import FirebaseAuth

class AuthorizationViewModel: ObservableObject {
    @Published var email = ""
    @Published var password = ""
    @Published var alert = ""
    @Published var isShowFilmsListView = false
    @Published var isShowAlert = false
    
    private let auth = Auth.auth()
    
    func hidePopup(){
        isShowAlert = false
    }
    
    func showAlert(message: String) {
        alert = message
        isShowAlert = true
    }
    
    func authorization() {
        guard password.count >= 8, !email.isEmpty else {
            showAlert(message: "Invalid data in input fields")
            return
        }
        
        auth.signIn(withEmail: email, password: password) { (authResult, error) in
            if let error = error {
                self.showAlert(message: error.localizedDescription)
                return
            }
            
            self.isShowFilmsListView = true
        }
    }
}

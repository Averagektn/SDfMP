import FirebaseAuth
import FirebaseDatabase
import Combine

class RegistrationViewModel: ObservableObject {
    @Published var login = ""
    @Published var email = ""
    @Published var password = ""
    @Published var alert = ""
    @Published var isShowFilmsListView = false
    @Published var isShowAlert = false
    
    private let auth = Auth.auth()
    private let database = Database.database().reference()
    
    func hidePopup(){
        isShowAlert = false
    }
    
    func showAlert(message: String) {
        alert = message
        isShowAlert = true
    }
    
    func registerUser() {
        guard login.count >= 4, password.count >= 8, !email.isEmpty else {
            showAlert(message: "Invalid data in input fields")
            return
        }
        
        auth.createUser(withEmail: email, password: password) { [weak self] result, error in
            guard let self = self else { return }
            
            if let _ = error {
                self.showAlert(message: "Error creating user")
                return
            }
            
            guard let user = result?.user else {
                self.showAlert(message: "No user created")
                return
            }
            
            let userData: [String: Any] = [
                "login": self.login,
                "email": self.email
            ]
            
            self.database.child("users").child(user.uid).setValue(userData) { error, _ in
                if error != nil {
                    self.showAlert(message: "Error saving user data")
                    return
                }
                
                self.isShowFilmsListView = true
            }
        }
    }
}

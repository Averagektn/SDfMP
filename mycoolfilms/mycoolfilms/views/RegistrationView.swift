import SwiftUI
import FirebaseAuth
import FirebaseDatabase

struct RegistrationView: View {
    @State private var login: String = ""
    @State private var email: String = ""
    @State private var password: String = ""
    @State private var showAuthorizationView = false
    @State private var showFilmsListView = false

    var body: some View {
        NavigationView {
            VStack {
                Text("Sign Up")
                    .font(.system(size: 40))
                    .fontWeight(.bold)
                    .padding(.bottom, 10)

                VStack {
                    TextField("Enter login", text: $login)
                        .padding()
                        .autocapitalization(.none)
                        .keyboardType(.default)
                    
                    TextField("Enter email", text: $email)
                        .padding()
                        .autocapitalization(.none)
                        .keyboardType(.emailAddress)

                    SecureField("Enter password", text: $password)
                        .padding()

                    Button(action: {
                        Auth.auth().createUser(withEmail: email, password: password) { _, error in
                            if error == nil {
                                // Registration successful
                                saveUserData()
                                showFilmsListView = true
                            }
                        }
                    }) {
                        Text("Sign Up")
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                            .padding()
                            .background(Color.blue)
                            .cornerRadius(10)
                    }
                    .padding(.vertical, 20)
                                        
                    NavigationLink(
                        destination: AuthorizationView().navigationBarBackButtonHidden(true),
                        isActive: $showAuthorizationView,
                        label: {
                            Button(action: {
                                showAuthorizationView = true
                            }) {
                                Text("Sign In")
                                    .fontWeight(.bold)
                                    .foregroundColor(.white)
                                    .padding()
                                    .background(Color.gray)
                                    .cornerRadius(10)
                            }
                            .padding(.vertical, 20)
                        }
                    )
                    
                    NavigationLink(
                        destination: FilmsListView().navigationBarBackButtonHidden(true),
                        isActive: $showFilmsListView,
                        label: { EmptyView() }
                    )
                }
                .padding(.horizontal, 15)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(Color.white)
            .edgesIgnoringSafeArea(.all)
            .navigationBarHidden(true)
            .navigationBarBackButtonHidden(true)
            .onAppear {
                UINavigationController.attemptRotationToDeviceOrientation()
            }
        }
    }
    
    private func saveUserData() {
        if let user = Auth.auth().currentUser {
            let uid = user.uid
            let userData: [String: Any] = [
                "login": login,
                "email": email
            ]
            let userRef = Database.database().reference().child("users").child(uid)
            userRef.setValue(userData)
        }
    }
}

struct RegistrationView_Previews: PreviewProvider {
    static var previews: some View {
        RegistrationView()
    }
}

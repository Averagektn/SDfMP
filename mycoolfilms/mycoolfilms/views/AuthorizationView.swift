import SwiftUI
import Firebase
import FirebaseAuth

struct AuthorizationView: View {
    @State private var email: String = ""
    @State private var password: String = ""
    @State private var showRegistrationView = false
    @State private var isLoggedIn = false
    
    var body: some View {
        NavigationView {
            VStack {
                Text("Sign In")
                    .font(.system(size: 40))
                    .fontWeight(.bold)
                    .padding(.bottom, 10)
                
                VStack {
                    TextField("Enter email", text: $email)
                        .padding()
                        .autocapitalization(.none)
                        .keyboardType(.emailAddress)
                    
                    SecureField("Enter password", text: $password)
                        .padding()
                    
                    Button(action: {
                        Auth.auth().signIn(withEmail: email, password: password) { result, error in
                            if error == nil {
                                isLoggedIn = true
                            }
                        }
                    }) {
                        Text("Sign In")
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                            .padding()
                            .background(Color.blue)
                            .cornerRadius(10)
                    }
                    .padding(.vertical, 20)
                    
                    NavigationLink(
                        destination: FilmsListView(),
                        isActive: $isLoggedIn,
                        label: {
                            EmptyView()
                        }
                    )
                    .hidden()
                    
                    Button(action: {
                        showRegistrationView = true
                    }) {
                        Text("Sign Up")
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                            .padding()
                            .background(Color.gray)
                            .cornerRadius(10)
                    }
                    .padding(.vertical, 20)
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
}

struct AuthorizationView_Previews: PreviewProvider {
    static var previews: some View {
        RegistrationView()
    }
}

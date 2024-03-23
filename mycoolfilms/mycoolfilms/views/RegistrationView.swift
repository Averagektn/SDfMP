import SwiftUI

struct RegistrationView: View {
    @State private var login: String = ""
    @State private var email: String = ""
    @State private var password: String = ""
    @State private var showAuthorizationView = false

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
                        // TODO sign in action
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
                        destination: AuthorizationView(),
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
                }
                .padding(.horizontal, 15)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(Color.white)
            .edgesIgnoringSafeArea(.all)
            .navigationBarHidden(true)
        }
    }
}

struct RegistrationView_Previews: PreviewProvider {
    static var previews: some View {
        RegistrationView()
    }
}

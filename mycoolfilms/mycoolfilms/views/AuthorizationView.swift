import SwiftUI

struct AuthorizationView: View {
    @State private var email: String = ""
    @State private var password: String = ""
    @State private var showRegistrationView = false
    
    var body: some View {
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
                    // TODO sign in action
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
                    destination: RegistrationView(),
                    isActive: $showRegistrationView,
                    label: {
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

struct AuthorizationView_Previews: PreviewProvider {
    static var previews: some View {
        RegistrationView()
    }
}

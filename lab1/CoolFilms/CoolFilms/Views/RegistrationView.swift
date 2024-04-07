import SwiftUI
import FirebaseAuth
import FirebaseDatabase
import Combine

struct RegistrationView: View {
    @EnvironmentObject var viewModel: RegistrationViewModel
    @StateObject private var authorizationViewModel = AuthorizationViewModel()
    @StateObject private var filmsListViewModel = FilmsListViewModel()
    
    var body: some View {
        NavigationView {
            VStack {
                Text("Sign Up")
                    .font(.system(size: 40))
                    .fontWeight(.bold)
                    .padding(.bottom, 10)
                VStack {
                    TextField("Enter login", text: $viewModel.login)
                        .padding()
                        .autocapitalization(.none)
                        .keyboardType(.default)
                    
                    TextField("Enter email", text: $viewModel.email)
                        .padding()
                        .autocapitalization(.none)
                        .keyboardType(.emailAddress)
                    
                    SecureField("Enter password", text: $viewModel.password)
                        .padding()
                    
                    NavigationLink(destination: FilmsListView().environmentObject(filmsListViewModel).navigationBarBackButtonHidden(true), isActive: $viewModel.isShowFilmsListView) {
                        Button(action: {
                            viewModel.registerUser()
                        }) {
                            Text("Sign Up")
                                .fontWeight(.bold)
                                .foregroundColor(.white)
                                .padding()
                                .background(Color.blue)
                                .cornerRadius(10)
                        }
                        .padding(.vertical, 20)
                    }
                }
                
                NavigationLink(destination: AuthorizationView().environmentObject(authorizationViewModel)
                    .navigationBarBackButtonHidden(true)) {
                    Text("Sign In")
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
        .alert(isPresented: $viewModel.isShowAlert) {
            Alert(
                title: Text("ERROR"),
                message: Text(viewModel.alert),
                dismissButton: .default(Text("OK"), action: {
                    viewModel.hidePopup()
                })
            )
        }
    }
}
    
#Preview {
    RegistrationView()
}

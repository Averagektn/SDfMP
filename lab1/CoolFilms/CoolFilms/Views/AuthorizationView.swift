import SwiftUI

struct AuthorizationView: View {
    @EnvironmentObject var viewModel: AuthorizationViewModel
    @StateObject private var registrationViewModel = RegistrationViewModel()
    @StateObject private var filmsListViewModel = FilmsListViewModel()
    
    var body: some View {
        NavigationView {
            VStack {
                Text("Sign In")
                    .font(.system(size: 40))
                    .fontWeight(.bold)
                    .padding(.bottom, 10)
                VStack {
                    TextField("Enter email", text: $viewModel.email)
                        .padding()
                        .autocapitalization(.none)
                        .keyboardType(.emailAddress)
                    
                    SecureField("Enter password", text: $viewModel.password)
                        .padding()
                    
                    NavigationLink(destination: FilmsListView().environmentObject(filmsListViewModel).navigationBarBackButtonHidden(true), isActive: $viewModel.isShowFilmsListView) {
                        Button(action: {
                            viewModel.authorization()
                        }) {
                            Text("Sign In")
                                .fontWeight(.bold)
                                .foregroundColor(.white)
                                .padding()
                                .background(Color.blue)
                                .cornerRadius(10)
                        }
                        .padding(.vertical, 20)
                    }
                }
                
                NavigationLink(destination: RegistrationView().environmentObject(registrationViewModel).navigationBarBackButtonHidden(true)) {
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

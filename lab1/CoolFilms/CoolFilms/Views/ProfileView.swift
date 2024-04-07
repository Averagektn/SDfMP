import SwiftUI
import FirebaseAuth
import FirebaseDatabaseInternal

struct ProfileView: View {
    @EnvironmentObject var viewModel: ProfileViewModel
    @State private var image: UIImage? = nil
    @State private var isShowingAlert = false
    @State private var notAuthorized = false
    @State private var isShowImagePicker = false
    
    var body: some View {
        ZStack {
            VStack {
                HStack {
                    Text("Email:")
                        .font(.headline)
                    TextField("empty", text: $viewModel.user.email)
                        .disableAutocorrection(true)
                        .autocapitalization(.none)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .trailing)
                    Button(action: {
                        isShowingAlert = true
                    }) {
                        Image(systemName: "nosign")
                            .foregroundColor(.red)
                            .padding()
                    }
                }
                .alert("Delete user/Log out", isPresented: $isShowingAlert) {
                    Button("Delete user", action: {
                        viewModel.deleteProfile()
                        notAuthorized = true
                        isShowingAlert = false
                    })
                    Button("Log Out", action: {
                        viewModel.logOut()
                        notAuthorized = true
                        isShowingAlert = false
                    })
                    Button("Cancel", action: {
                        isShowingAlert = false
                    })
                }
                .fullScreenCover(isPresented: $notAuthorized, content: {
                    RegistrationView().environmentObject(RegistrationViewModel())
                })
                
                HStack {
                    Text("Login:")
                        .font(.headline)
                    TextField("empty", text: $viewModel.user.login)
                        .disableAutocorrection(true)
                        .autocapitalization(.none)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }
                
                HStack {
                    Text("Information:")
                        .font(.headline)
                    TextField("empty", text: $viewModel.user.information)
                        .disableAutocorrection(true)
                        .autocapitalization(.none)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }
                
                HStack {
                    Text("Name:")
                        .font(.headline)
                    TextField("empty", text: $viewModel.user.username)
                        .disableAutocorrection(true)
                        .autocapitalization(.none)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }
                
                HStack {
                    Text("Surname:")
                        .font(.headline)
                    TextField("empty", text: $viewModel.user.surname)
                        .disableAutocorrection(true)
                        .autocapitalization(.none)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }
                
                HStack {
                    Text("Patronymic:")
                        .font(.headline)
                    TextField("empty", text: $viewModel.user.patronymic)
                        .disableAutocorrection(true)
                        .autocapitalization(.none)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }
                
                HStack {
                    Text("Gender:")
                        .font(.headline)
                    TextField("empty", text: $viewModel.user.gender)
                        .disableAutocorrection(true)
                        .autocapitalization(.none)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }
                
                HStack {
                    Text("Country:")
                        .font(.headline)
                    TextField("empty", text: $viewModel.user.country)
                        .disableAutocorrection(true)
                        .autocapitalization(.none)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }
                
                HStack {
                    Text("Favored Film:")
                        .font(.headline)
                    TextField("empty", text: $viewModel.user.favoredFilm)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }
                
                HStack {
                    Text("Favored Genre:")
                        .font(.headline)
                    TextField("empty", text: $viewModel.user.favoredGenre)
                        .padding()
                        .frame(maxWidth: .infinity, alignment: .trailing)
                }
                
                HStack {
                    NavigationLink(destination: FavoredView().environmentObject(FavoredViewModel())) {
                        Text("Favored")
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                            .padding()
                            .background(Color.blue)
                            .cornerRadius(10)
                    }
                    
                    Button(action: {
                        viewModel.update()
                    }) {
                        Text("Update")
                            .padding()
                            .foregroundColor(.white)
                            .background(Color.green)
                            .cornerRadius(10)
                    }
                    
                    NavigationLink(destination: FilmsListView().environmentObject(FilmsListViewModel())) {
                        Text("Films")
                            .fontWeight(.bold)
                            .foregroundColor(.white)
                            .padding()
                            .background(Color.blue)
                            .cornerRadius(10)
                    }
                }
                .padding()
            }
            .padding()
        }
        .onAppear {
            viewModel.loadData()
        }
    }
}

#Preview {
    ProfileView()
}

import SwiftUI

struct FavoredView: View {
    @EnvironmentObject private var viewModel: FavoredViewModel
    
    @StateObject private var filmsListViewModel = FilmsListViewModel()
    @StateObject private var profileViewModel = ProfileViewModel()
    
    @State private var searchText = ""
    
    var body: some View {
        VStack {
            TextField("Search", text: $searchText)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            
            List(viewModel.films.filter { film in
                searchText.isEmpty || film.name.lowercased().contains(searchText.lowercased())
            }) { film in
                FilmRow(viewModel: FilmViewModel(film: film))
            }
            
            Spacer()
            
            HStack {
                NavigationLink(destination: FilmsListView().environmentObject(filmsListViewModel)) {
                    Text("Films")
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.blue)
                        .cornerRadius(10)
                }
                
                NavigationLink(destination: ProfileView().environmentObject(profileViewModel)) {
                    Text("Profile")
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.blue)
                        .cornerRadius(10)
                }
            }
        }
        .onAppear {
            viewModel.loadFilms()
        }
    }
}

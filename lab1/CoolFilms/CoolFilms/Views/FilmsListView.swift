import SwiftUI
import SDWebImageSwiftUI

struct FilmsListView: View {
    @EnvironmentObject private var viewModel: FilmsListViewModel
    
    @StateObject private var favoredViewModel = FavoredViewModel()
    
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
                NavigationLink(destination: FavoredView().environmentObject(favoredViewModel)) {
                    Text("Favored")
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.blue)
                        .cornerRadius(10)
                }
                
                NavigationLink(destination: ProfileView().environmentObject(ProfileViewModel())) {
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
     
#Preview {
    FilmsListView()
}

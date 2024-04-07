import SwiftUI

struct FilmView: View {
    @ObservedObject private var viewModel: FilmViewModel
    @State private var isAddedToFavored = false
    
    init(film: Film) {
        self.viewModel = FilmViewModel(film: film)
    }
    
    var body: some View {
        VStack {
            HStack {
                Text(viewModel.film.name)
                    .font(.title)
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity, alignment: .leading)
                
                Button(action: {
                    viewModel.removeFromFavored()
                    isAddedToFavored = false
                }) {
                    Image(systemName: "nosign")
                        .foregroundColor(.red)
                }
                .frame(width: 30, height: 30)
                .opacity(!isAddedToFavored ? 0.0 : 1.0)
                .disabled(!isAddedToFavored)
                
                Button(action: {
                    viewModel.addToFavored()
                    isAddedToFavored = true
                }) {
                    Image(systemName: "star.fill")
                }
                .frame(width: 30, height: 30)
                .opacity(isAddedToFavored ? 0.0 : 1.0)
                .disabled(isAddedToFavored)
            }
            
            TabView {
                ForEach(viewModel.images, id: \.self) { image in
                    Image(uiImage: image)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                }
            }
            .tabViewStyle(PageTabViewStyle())
            .frame(height: 200)
            
            Text(viewModel.film.categories.joined(separator: ", "))
                .font(.headline)
                .frame(maxWidth: .infinity, alignment: .center)
            
            Text(viewModel.film.description)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            List(viewModel.comments) { comment in
                CommentView(comment: comment)
            }
            .frame(maxHeight: .infinity)
            
            HStack {
                TextField("Enter comment", text: $viewModel.newComment)
                
                Button(action: viewModel.addComment) {
                    Text("Send")
                }
            }
            .padding()
            
            HStack {
                NavigationLink(destination: FilmsListView().environmentObject(FilmsListViewModel())) {
                    Text("Films")
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
                
                NavigationLink(destination: FavoredView().environmentObject(FavoredViewModel())) {
                    Text("Favored")
                        .fontWeight(.bold)
                        .foregroundColor(.white)
                        .padding()
                        .background(Color.blue)
                        .cornerRadius(10)
                }
            }
        }
        .padding()
        .onAppear{
            viewModel.getComments()
            viewModel.getSlider()
            viewModel.checkIfFilmIdExists(completion: { isInFavored in
                isAddedToFavored = isInFavored
            })
        }
    }
}

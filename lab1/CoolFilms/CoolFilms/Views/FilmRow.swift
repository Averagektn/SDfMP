import SwiftUI

struct FilmRow: View {
    @ObservedObject var viewModel: FilmViewModel
    
    @State private var image: UIImage? = nil
    
    var body: some View {
        NavigationLink(destination: FilmView(film: viewModel.film)) {
            HStack {
                if let image = image {
                    Image(uiImage: image)
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 100, height: 100)
                        .foregroundColor(.gray)
                } else {
                    Image(systemName: "photo")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 100, height: 100)
                        .foregroundColor(.gray)
                }
                
                VStack(alignment: .leading) {
                    Text(viewModel.film.name)
                        .font(.headline)
                    
                    Text(viewModel.film.categories.joined(separator: ", "))
                        .font(.subheadline)
                        .foregroundColor(.gray)
                }
            }
            .padding()
            .onAppear {
                viewModel.loadImage()
                viewModel.getImage() { loadedImage in
                    DispatchQueue.main.async {
                        image = loadedImage
                    }
                }
            }
        }
    }
}

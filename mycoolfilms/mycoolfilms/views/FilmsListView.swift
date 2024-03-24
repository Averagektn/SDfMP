import SwiftUI

struct FilmsListView: View {
    var body: some View {
        VStack {
            List {
                ForEach(0..<films.count) { index in
                    FilmListItemView(film: films[index])
                }
            }
            
            HStack {
                Button(action: {
                    // Действия при нажатии на кнопку "To Profile"
                }) {
                    Text("To Profile")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                }
                
                Button(action: {
                    // Действия при нажатии на кнопку "To Films List"
                }) {
                    Text("To Films List")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                }
            }
            .padding(.vertical)
        }
        .navigationBarTitle("Films", displayMode: .inline)
    }
}

struct FilmListItemView: View {
    let film: Film
    
    var body: some View {
        HStack {
            Image("filmImage")
                .resizable()
                .frame(width: 64, height: 60)
                .padding(.horizontal, 10)
            
            VStack(alignment: .leading) {
                Text(film.name)
                    .font(.headline)
                
                Text(film.categories)
                    .font(.subheadline)
            }
            
            Spacer()
            
            Button(action: {
                // Действия при нажатии на кнопку "Delete"
            }) {
                Image(systemName: "trash")
                    .foregroundColor(.red)
            }
        }
    }
}

struct Film: Identifiable {
    let id = UUID()
    let name: String
    let categories: String
}

let films: [Film] = [
    Film(name: "Film 1", categories: "Category 1"),
    Film(name: "Film 2", categories: "Category 2"),
    Film(name: "Film 3", categories: "Category 3")
]

struct FilmsListView_Previews: PreviewProvider {
    static var previews: some View {
        FilmsListView()
    }
}

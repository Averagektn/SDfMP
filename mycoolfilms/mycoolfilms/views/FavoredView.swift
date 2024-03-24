import SwiftUI

struct FavoredView: View {
    var body: some View {
        ZStack {
            Color.white.edgesIgnoringSafeArea(.all)
            
            VStack {
                ScrollView {
                    LazyVStack(spacing: 10) {
                        ForEach(0..<10) { index in
                            FilmListItem()
                        }
                    }
                }
                
                HStack {
                    Button(action: {}) {
                        Text("To Profile")
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color.blue)
                            .foregroundColor(.white)
                            .font(.headline)
                    }
                    
                    Button(action: {}) {
                        Text("To Films List")
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color.blue)
                            .foregroundColor(.white)
                            .font(.headline)
                    }
                }
                .padding(.vertical)
            }
            .padding()
        }
    }
}

struct FilmListItem: View {
    var body: some View {
        HStack {
            Image("filmImage") // Replace with your image name
                .resizable()
                .frame(width: 64, height: 60)
                .cornerRadius(8)
                .padding(.trailing, 10)
            
            VStack(alignment: .leading) {
                Text("Film Name")
                    .font(.title)
                
                Text("Film Categories")
                    .font(.subheadline)
            }
            
            Spacer()
            
            Button(action: {}) {
                Image(systemName: "trash")
                    .font(.title)
                    .foregroundColor(.red)
            }
        }
        .padding(.vertical, 10)
    }
}

struct FavoredView_Previews: PreviewProvider {
    static var previews: some View {
        FavoredView()
    }
}

import SwiftUI

struct ContentView: View {
    var body: some View {
        NavigationView {
            FilmView()
                .navigationBarTitle("Film Details", displayMode: .inline)
        }
    }
}

struct FilmView: View {
    var body: some View {
        VStack {
            HStack {
                Text("Film Name")
                    .font(.system(size: 75))
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity)
                
                Button(action: {
                    // Действия при нажатии на кнопку "Add to Favored"
                }) {
                    Image(systemName: "star.fill")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 30, height: 30)
                        .padding()
                }
            }
            
            SliderView()
            
            Text("Categories")
                .fontWeight(.bold)
                .frame(maxWidth: .infinity)
                .padding()
            
            Text("Description")
                .frame(maxWidth: .infinity)
                .padding()
            
            Spacer()
            
            HStack {
                Button(action: {
                    // Действия при нажатии на кнопку "To Favored"
                }) {
                    Text("To Favored")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                        .padding(.horizontal, 7)
                }
                
                Button(action: {
                    // Действия при нажатии на кнопку "To Films List"
                }) {
                    Text("To Films List")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                        .padding(.horizontal, 7)
                }
                
                Button(action: {
                    // Действия при нажатии на кнопку "To Profile"
                }) {
                    Text("To Profile")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                        .padding(.horizontal, 7)
                }
            }
            .padding(.vertical)
        }
        .navigationBarHidden(true)
    }
}

struct SliderView: View {
    var body: some View {
        TabView {
            ForEach(0..<sliderImages.count) { index in
                Image(sliderImages[index])
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(height: 300)
                    .clipped()
            }
        }
        .tabViewStyle(PageTabViewStyle())
        .indexViewStyle(PageIndexViewStyle(backgroundDisplayMode: .always))
        .frame(height: 300)
    }
}

let sliderImages = [
    "image1",
    "image2",
    "image3"
]

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

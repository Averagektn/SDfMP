import SwiftUI

struct ProfileView: View {
    var body: some View {
        VStack {
            Group {
                HStack {
                    Text("Email")
                        .font(.title) // Increase the font size
                    Spacer()
                    Button(action: {}) {
                        Image(systemName: "trash")
                            .font(.title) // Increase the image size
                    }
                }
                
                HStack {
                    Text("Login")
                        .font(.title) // Increase the font size
                    Spacer()
                    Button(action: {}) {
                        Image(systemName: "pencil")
                            .font(.title) // Increase the image size
                    }
                }
                
                HStack {
                    Text("Birth Date")
                        .font(.title) // Increase the font size
                    Spacer()
                    Button(action: {}) {
                        Image(systemName: "pencil")
                            .font(.title) // Increase the image size
                    }
                }
                
                HStack {
                    Text("Information")
                        .font(.title) // Increase the font size
                    Spacer()
                    Button(action: {}) {
                        Image(systemName: "pencil")
                            .font(.title) // Increase the image size
                    }
                }
                
                HStack {
                    Text("Name")
                        .font(.title) // Increase the font size
                    Spacer()
                    Button(action: {}) {
                        Image(systemName: "pencil")
                            .font(.title) // Increase the image size
                    }
                }
                
                HStack {
                    Text("Surname")
                        .font(.title) // Increase the font size
                    Spacer()
                    Button(action: {}) {
                        Image(systemName: "pencil")
                            .font(.title) // Increase the image size
                    }
                }
                
                HStack {
                    Text("Patronymic")
                        .font(.title) // Increase the font size
                    Spacer()
                    Button(action: {}) {
                        Image(systemName: "pencil")
                            .font(.title) // Increase the image size
                    }
                }
                
                HStack {
                    Text("Gender")
                        .font(.title) // Increase the font size
                    Spacer()
                    Button(action: {}) {
                        Image(systemName: "pencil")
                            .font(.title) // Increase the image size
                    }
                }
                
                HStack {
                    Text("Country")
                        .font(.title) // Increase the font size
                    Spacer()
                    Button(action: {}) {
                        Image(systemName: "pencil")
                            .font(.title) // Increase the image size
                    }
                }
            }
            
            Spacer()
            
            HStack {
                Button(action: {}) {
                    Text("To Favored")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .font(.title) // Increase the font size
                }
                
                Button(action: {}) {
                    Text("To Films List")
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color.blue)
                        .foregroundColor(.white)
                        .font(.title) // Increase the font size
                }
            }
        }
    }
}

struct ProfileView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileView()
    }
}

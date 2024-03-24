import SwiftUI

struct FilmsListView: View {
    var body: some View {
        Text("Films List")
            .navigationBarTitle("Films")
            .navigationBarBackButtonHidden(true)
            .onAppear {
                UINavigationController.attemptRotationToDeviceOrientation()
            }
    }
}

struct FilmsListView_Previews: PreviewProvider {
    static var previews: some View {
        FilmsListView()
    }
}

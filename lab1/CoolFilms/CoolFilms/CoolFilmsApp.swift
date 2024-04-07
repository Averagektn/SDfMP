import SwiftUI
import FirebaseCore

class AppDelegate: NSObject, UIApplicationDelegate {
  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {

    FirebaseApp.configure()

    return true
  }
}

@main
struct CoolFilmsApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    @StateObject private var registrationViewModel = RegistrationViewModel()

     var body: some Scene {
         WindowGroup {
             NavigationView {
                 RegistrationView()
                     .environmentObject(registrationViewModel)
             }
         }
     }
}

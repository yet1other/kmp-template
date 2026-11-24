import SwiftUI
import SharedUI

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) private var appDelegate

    var body: some Scene {
        WindowGroup {
            ContentView(app: appDelegate.app)
        }
    }
}

/// Owns the Kotlin app (graph and root component) for the process and hands its state to iOS state restoration.
final class AppDelegate: NSObject, UIApplicationDelegate {
    let app = IosApp()

    func application(_ application: UIApplication, shouldSaveSecureApplicationState coder: NSCoder) -> Bool {
        app.saveState(coder: coder)
        return true
    }

    func application(_ application: UIApplication, shouldRestoreSecureApplicationState coder: NSCoder) -> Bool {
        app.restoreState(coder: coder)
        return true
    }
}

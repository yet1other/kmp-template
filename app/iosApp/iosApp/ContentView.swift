import UIKit
import SwiftUI
import SharedUI

struct ComposeView: UIViewControllerRepresentable {
    let app: IosApp

    func makeUIViewController(context: Self.Context) -> UIViewController {
        app.mainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Self.Context) {}
}

struct ContentView: View {
    let app: IosApp

    var body: some View {
        ComposeView(app: app)
            .ignoresSafeArea()
    }
}

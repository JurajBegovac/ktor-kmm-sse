import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        DependencyInjectionKt.doInitKmmDependencyInjection()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}

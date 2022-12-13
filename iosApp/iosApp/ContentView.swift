import SwiftUI
import shared

struct ContentView: View {
    @StateObject var viewModel = ViewModel(sseDataSource: KmmDependencyInjection().sseDataSource)

    var body: some View {
        TabView {
            SseListView(viewModel: viewModel)
        }
    }
}

struct SseListView: View {
    @ObservedObject var viewModel: ViewModel

    var body: some View {
        NavigationView {
            List(viewModel.sseEvents, id: \.id) { sseEvent in
                Text("\(sseEvent)")
            }
                    .navigationBarTitle(Text("Sse"))
                    .navigationBarTitleDisplayMode(.inline)
                    .task {
                        await viewModel.observeSse()
                    }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

extension View {
    @available(iOS, deprecated: 15.0, message: "This extension is no longer necessary. Use API built into SDK")
    func task(priority: TaskPriority = .userInitiated, _ action: @escaping @Sendable () async -> Void) -> some View {
        self.onAppear {
            Task(priority: priority) {
                await action()
            }
        }
    }
}

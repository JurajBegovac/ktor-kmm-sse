//
//  ViewModel.swift
//  iosApp
//
//  Created by Juraj Begovac on 12.12.2022..
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesAsync

@MainActor
class ViewModel: ObservableObject {
    @Published var sseEvents = [Shared_modelsSseEvent]()
    
    private let sseDataSource: SseDataSource
    
    init(sseDataSource: SseDataSource) {
        self.sseDataSource = sseDataSource
    }
    
    func observeSse() async {
        do {
            let stream = asyncStream(for: sseDataSource.observeNative())
            for try await data in stream {
                self.sseEvents.append(data)
            }
        } catch {
            print("Failed with error: \(error)")
        }
    }
}

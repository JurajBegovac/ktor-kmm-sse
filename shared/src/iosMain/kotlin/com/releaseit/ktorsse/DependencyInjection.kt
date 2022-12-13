package com.releaseit.ktorsse

import com.releaseit.ktorsse.data.SseDataSource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

public fun initKmmDependencyInjection() {
    startKoin {
        modules(sharedModule())
    }
}

public class KmmDependencyInjection : KoinComponent {
    public val sseDataSource: SseDataSource by inject()
}

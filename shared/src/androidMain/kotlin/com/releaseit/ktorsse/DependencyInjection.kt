package com.releaseit.ktorsse

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

public fun initDependencyInjection(appDeclaration: KoinAppDeclaration = {}): KoinApplication =
    startKoin(appDeclaration)
        .modules(sharedModule())

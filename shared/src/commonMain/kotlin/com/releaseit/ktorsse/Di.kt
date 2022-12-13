package com.releaseit.ktorsse

import com.releaseit.ktorsse.data.SseDataSource
import com.releaseit.ktorsse.data.SseDataSourceImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

internal fun sharedModule(): Module = module {
    single<ApiConfig> { ApiConfigImpl() }

    single {
        Json {
            encodeDefaults = false
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    single {
        HttpClient {
            defaultRequest {
                url(get<ApiConfig>().baseEndpoint)
            }
            install(Logging) {
                level = LogLevel.HEADERS
                logger = Logger.SIMPLE
            }
            install(ContentNegotiation) {
                json(get())
            }
        }
    }

    single<SseDataSource> { SseDataSourceImpl(get(), get()) }
}



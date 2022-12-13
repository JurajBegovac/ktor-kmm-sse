package com.releaseit.ktorsse.data

import com.releaseit.ktorsse.ApiConfig
import com.releaseit.shared_models.sse.SseEvent
import com.releaseit.ktorsse.sse.readSse
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

public interface SseDataSource {
    public fun observe(): Flow<SseEvent>
}

internal class SseDataSourceImpl(
    private val httpClient: HttpClient,
    private val apiConfig: ApiConfig
) : SseDataSource {

    override fun observe(): Flow<SseEvent> =
        httpClient.readSse(url = apiConfig.sseEndpoint)
}

package com.releaseit.ktorsse.sse

import com.releaseit.shared_models.sse.EventId
import com.releaseit.shared_models.sse.SseEvent
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.HttpStatement
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

internal const val HEADER_LAST_EVENT_ID = "Last-Event-Id"

public typealias Milliseconds = Long

internal typealias HeadersProvider = suspend (EventId?) -> Map<String, String>
internal typealias QueryParamsProvider = suspend (EventId?) -> Map<String, String>

public class UnauthorizedError : Throwable()
public class NotEventStreamError : Throwable()

public fun HttpClient.readSse(
    url: String,
    headersProvider: HeadersProvider = { it?.let { mapOf(HEADER_LAST_EVENT_ID to it) } ?: emptyMap() },
    queryParamsProvider: QueryParamsProvider = { emptyMap() },
    defaultReconnectDelayMillis: Milliseconds = 3000L
): Flow<SseEvent> {
    var reconnectDelay: Milliseconds = defaultReconnectDelayMillis
    var lastEventId: String? = null
    return flow {
        coroutineScope {
            while (isActive) {
                val customHeaders = headersProvider(lastEventId)
                val queryParams = queryParamsProvider(lastEventId)
                prepareRequest(
                    url = url,
                    headers = customHeaders,
                    queryParams = queryParams
                ).execute { response ->
                    if (!response.status.isSuccess()) {
                        throw UnauthorizedError()
                    }
                    if (!response.isEventStream()) {
                        throw NotEventStreamError()
                    }
                    response.bodyAsChannel()
                        .readSse(
                            onSseEvent = { sseEvent ->
                                lastEventId = sseEvent.id
                                emit(sseEvent)
                            },
                            onRetryChanged = {
                                reconnectDelay = it
                            }
                        )
                }
                delay(reconnectDelay)
            }
        }
    }
}

private suspend fun HttpClient.prepareRequest(
    url: String,
    headers: Map<String, String> = emptyMap(),
    queryParams: Map<String, String> = emptyMap()
): HttpStatement =
    prepareGet(url) {
        headers {
            append(HttpHeaders.Accept, "text/event-stream")
            append(HttpHeaders.CacheControl, "no-cache")
            append(HttpHeaders.Connection, "keep-alive")
            headers.forEach { (key, value) -> append(key, value) }
        }
        queryParams.forEach { (key, value) -> addOrReplaceParameter(key, value) }
    }

private fun HttpResponse.isEventStream(): Boolean {
    val contentType = contentType() ?: return false
    return contentType.contentType == "text" && contentType.contentSubtype == "event-stream"
}

private fun HttpRequestBuilder.addOrReplaceParameter(key: String, value: String?): Unit =
    value?.let {
        url.parameters.remove(key)
        url.parameters.append(key, it)
    } ?: Unit

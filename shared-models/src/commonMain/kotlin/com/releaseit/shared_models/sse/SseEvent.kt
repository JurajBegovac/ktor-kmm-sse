package com.releaseit.shared_models.sse

public typealias EventType = String
public typealias EventData = String
public typealias EventId = String

public data class SseEvent(
    val id: EventId? = null,
    val event: EventType? = null,
    val data: EventData = ""
)

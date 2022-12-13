package com.releaseit.backend

import com.releaseit.shared_models.Constants
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.cacheControl
import io.ktor.server.response.respondTextWriter
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.broadcast
import kotlinx.coroutines.channels.produce

@Suppress("OPT_IN_USAGE")
fun main() {
    embeddedServer(Netty, port = Constants.PORT, host = Constants.IP_ADDRESS) {
        val channel = produce {
            var n = 0
            while (true) {
                send(SseEvent(id = n.toString(), data = "This is sse event number:$n"))
                if (n % 5 == 4) {
                    // on 3000 millis we have reconnection in SSE so let's delay each 5th element for 4000
                    delay(4000)
                } else {
                    delay(1000)
                }
                n++
            }
        }.broadcast()

        routing {
            get("/${Constants.SSE_PATH}") {
                val events = channel.openSubscription()
                try {
                    call.respondSse(events)
                } finally {
                    events.cancel()
                }
            }

        }
    }.start(wait = true)
}

data class SseEvent(val data: String, val event: String? = null, val id: String? = null)

suspend fun ApplicationCall.respondSse(events: ReceiveChannel<SseEvent>) {
    response.cacheControl(CacheControl.NoCache(null))
    respondTextWriter(contentType = ContentType.Text.EventStream) {
        for (event in events) {
            if (event.id != null) {
                write("id: ${event.id}\n")
            }
            if (event.event != null) {
                write("event: ${event.event}\n")
            }
            for (dataLine in event.data.lines()) {
                write("data: $dataLine\n")
            }
            write("\n")
            flush()
        }
    }
}

package com.releaseit.ktorsse

import com.releaseit.shared_models.Constants

internal interface ApiConfig {
    val baseEndpoint: String
    val sseEndpoint: String
}

internal class ApiConfigImpl : ApiConfig {
    override val baseEndpoint: String = "http://${Constants.IP_ADDRESS}:${Constants.PORT}"

    override val sseEndpoint: String = Constants.SSE_PATH
}

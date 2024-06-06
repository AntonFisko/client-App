package com.example.clientapp

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WebSocketClient {
    private val client = HttpClient {
        install(WebSockets)
    }
    private var session: DefaultClientWebSocketSession? = null
    private val _messages = MutableStateFlow<String>("")

    val messages = _messages.asStateFlow()

    suspend fun connect(ip: String, port: String) {
        client.ws(
            method = HttpMethod.Get,
            host = ip,
            port = port.toInt(),
            path = "/ws"
        ) {
            session = this
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    _messages.value = frame.readText()
                }
            }
        }
    }

    suspend fun send(message: String) {
        session?.send(Frame.Text(message))
    }

    fun setOnMessageListener(listener: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            messages.collect { message ->
                listener(message)
            }
        }
    }

    suspend fun disconnect() {
        session?.close()
        client.close()
    }
}

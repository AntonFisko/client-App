package com.example.clientapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientapp.models.GestureCommand
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ClientViewModel(private val context: Context) : ViewModel() {
    private var client: HttpClient? = null
    private val _isClientRunning = MutableStateFlow(false)
    val isClientRunning: StateFlow<Boolean> get() = _isClientRunning

    private val _log = MutableStateFlow<List<String>>(emptyList())
    val log: StateFlow<List<String>> get() = _log

    fun connect(ip: String, port: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                client = HttpClient {
                    install(WebSockets)
                }
                client?.webSocket(host = ip, port = port, path = "/ws") {
                    _isClientRunning.value = true
                    _log.value = _log.value + "Connected to server at $ip:$port"
                    Log.d("ClientViewModel", "Connected to server at $ip:$port")

                    openChrome()

                    // Отправка сообщения о том, что Chrome открыт
                    val chromeOpenedCommand = GestureCommand("CHROME_OPENED", 0)
                    outgoing.send(Frame.Text(Json.encodeToString(chromeOpenedCommand)))

                    for (message in incoming) {
                        if (message is Frame.Text) {
                            val receivedText = message.readText()
                            val command = Json.decodeFromString<GestureCommand>(receivedText)
                            handleGestureCommand(command)
                        }
                    }
                }
            } catch (e: Exception) {
                _log.value = _log.value + "Failed to connect: ${e.localizedMessage}"
            }
        }
    }

    private fun openChrome() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("http://www.google.com")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun handleGestureCommand(command: GestureCommand) {
        val intent = Intent(context, GestureAccessibilityService::class.java)
        context.startService(intent)

        when (command.type) {
            "SWIPE_UP" -> {
                val service = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as GestureAccessibilityService
                service.performSwipeUp(command.duration)
            }
            "SWIPE_DOWN" -> {
                val service = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as GestureAccessibilityService
                service.performSwipeDown(command.duration)
            }
            else -> Log.e("ClientViewModel", "Unknown gesture command: ${command.type}")
        }
    }
}

package com.example.clientapp

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientapp.models.GestureCommand
import com.example.clientapp.models.GestureResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ClientViewModel(private val context: Context) : ViewModel() {

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _gestureResult = MutableStateFlow<GestureResult?>(null)
    val gestureResult: StateFlow<GestureResult?> = _gestureResult

    private var client: HttpClient? = null

    fun connectToServer(ip: String, port: Int) {
        viewModelScope.launch {
            client = HttpClient {
                install(WebSockets)
            }

            try {
                client?.webSocket(host = ip, port = port, path = "/ws") {
                    _isConnected.value = true
                    Log.d("ClientViewModel", "Connected to server at $ip:$port")
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val command = Json.decodeFromString<GestureCommand>(frame.readText())
                        performGesture(command)
                    }
                }
            } catch (e: Exception) {
                _isConnected.value = false
                Log.e("ClientViewModel", "Failed to connect to server: ${e.localizedMessage}")
            }
        }
    }

    fun disconnectFromServer() {
        viewModelScope.launch {
            client?.close()
            _isConnected.value = false
            Log.d("ClientViewModel", "Disconnected from server")
        }
    }

    private fun performGesture(command: GestureCommand) {
        val intent = Intent(context, MyAccessibilityService::class.java)
        context.startService(intent)

        // Directly access the service instance if available
        val service = MyAccessibilityService()
        service.performSwipeGesture(command.type, command.duration)

        val result = GestureResult("SUCCESS", "Gesture executed: ${command.type} for ${command.duration}ms")
        _gestureResult.value = result

        Log.d("ClientViewModel", "Gesture executed: ${command.type} for ${command.duration}ms")
    }
}
package com.example.clientapp.presentation.viewModel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientapp.presentation.accessibility.GestureAccessibilityService
import com.example.clientapp.domain.models.GestureCommand
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
                    _log.value += "Подключено к серверу на $ip:$port"
                    Log.d("ClientViewModel", "Подключено к серверу на $ip:$port")

                    openChrome()

                    val chromeOpenedCommand = GestureCommand("CHROME_OPENED", 0)
                    outgoing.send(Frame.Text(Json.encodeToString(chromeOpenedCommand)))

                    for (message in incoming) {
                        if (message is Frame.Text) {
                            val receivedText = message.readText()
                            Log.d("ClientViewModel", "Получено сообщение: $receivedText")
                            val command = Json.decodeFromString<GestureCommand>(receivedText)
                            handleGestureCommand(command)
                        }
                    }
                }
            } catch (e: Exception) {
                _log.value += "Ошибка подключения: ${e.localizedMessage}"
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
        val intent = Intent(context, GestureAccessibilityService::class.java).apply {
            putExtra("GESTURE_COMMAND_TYPE", command.type)
            putExtra("GESTURE_COMMAND_DURATION", command.duration)
        }
        context.startService(intent)
    }

    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            client?.close()
            client = null
            _isClientRunning.value = false
            _log.value += "Отключено от сервера"
        }
    }

    fun shutdownServer() {
        viewModelScope.launch(Dispatchers.IO) {
            client?.webSocket("/ws") {
                val shutdownCommand = GestureCommand("SHUTDOWN", 0)
                outgoing.send(Frame.Text(Json.encodeToString(shutdownCommand)))
                _log.value += "Отправлена команда выключения серверу"
            }
        }
    }
}
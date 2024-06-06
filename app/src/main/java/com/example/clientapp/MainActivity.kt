package com.example.clientapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    private val clientViewModel: ClientViewModel by viewModels {
        ClientViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClientApp(clientViewModel)
        }
    }
}

@Composable
fun ClientApp(viewModel: ClientViewModel) {
    var serverIp by remember { mutableStateOf("192.168.1.100") }
    var serverPort by remember { mutableStateOf("8080") }
    val isConnected by viewModel.isConnected.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = serverIp,
            onValueChange = { serverIp = it },
            label = { Text("Server IP") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = serverPort,
            onValueChange = { serverPort = it },
            label = { Text("Server Port") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                viewModel.connectToServer(serverIp, serverPort.toInt())
            },
            enabled = !isConnected,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Connect")
        }
        Button(
            onClick = {
                viewModel.disconnectFromServer()
            },
            enabled = isConnected,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Disconnect")
        }
        Text(text = if (isConnected) "Connected to server" else "Not connected")
    }
}
package com.example.clientapp.presentation.ui

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
import com.example.clientapp.presentation.viewModel.ClientViewModel

@Composable
fun ClientApp(viewModel: ClientViewModel) {
    var ip by remember { mutableStateOf("192.168.100.211") }
    var port by remember { mutableStateOf("8080") }
    val logs by viewModel.log.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = ip,
            onValueChange = { ip = it },
            label = { Text("IP сервера") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = port,
            onValueChange = { port = it },
            label = { Text("Порт сервера") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                viewModel.connect(ip, port.toInt())
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Подключиться")
        }
        logs.forEach { log ->
            Text(text = log)
        }
    }
}
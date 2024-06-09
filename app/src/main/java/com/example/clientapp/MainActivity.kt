package com.example.clientapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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

class MainActivity : ComponentActivity() {
    private val clientViewModel: ClientViewModel by viewModels {
        ClientViewModelFactory(applicationContext)
    }

    private fun promptUserToEnableAccessibilityService() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        this.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        promptUserToEnableAccessibilityService()

        setContent {
            ClientApp(clientViewModel)
        }
    }
}

@Composable
fun ClientApp(viewModel: ClientViewModel) {
    var ip by remember { mutableStateOf("192.168.100.211") }
    var port by remember { mutableStateOf("8080") }
//    val isClientRunning by viewModel.isClientRunning.collectAsState()
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
            label = { Text("Server IP") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = port,
            onValueChange = { port = it },
            label = { Text("Server Port") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                viewModel.connect(ip, port.toInt())
            },
//            enabled = !isClientRunning,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Connect")
        }
        logs.forEach { log ->
            Text(text = log)
        }
    }
}
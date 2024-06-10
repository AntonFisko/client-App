package com.example.clientapp.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.clientapp.presentation.viewModel.ClientViewModelFactory
import com.example.clientapp.presentation.viewModel.ClientViewModel

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
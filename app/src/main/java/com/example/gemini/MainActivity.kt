package com.example.gemini

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gemini.ui.theme.GeminiTheme

class MainActivity : ComponentActivity() {
    private val requestBluetoothPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions[Manifest.permission.BLUETOOTH_CONNECT] == true &&
                    permissions[Manifest.permission.BLUETOOTH_SCAN] == true &&
                    permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        } else {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        }

        if (!allGranted) {
            Toast.makeText(this, "Bluetooth permissions denied", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestBluetoothPermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        } else {
            requestBluetoothPermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }

        setContent {
            GeminiTheme {
                var showChatbot by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (showChatbot) {
                            ChatbotScreen()
                        } else {
                            ControlScreen()
                        }

                        // Toggle button
                        if (!showChatbot) {
                            IconButton(
                                onClick = { showChatbot = true },
                                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_chat_bot),
                                    contentDescription = "Chatbot"
                                )
                            }
                        } else {
                            IconButton(
                                onClick = { showChatbot = false },
                                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_home),
                                    contentDescription = "Home"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
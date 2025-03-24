package com.example.gemini

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ControlScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bluetoothController = remember { BluetoothController(context) }
    var isConnected by remember { mutableStateOf(false) }
    var connectionStatus by remember { mutableStateOf("Not Connected") }

    LaunchedEffect(Unit) {
        isConnected = bluetoothController.isConnected()
        connectionStatus = if (isConnected) "Connected to HC-05" else "Not Connected"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("AgroBot Controller", style = MaterialTheme.typography.headlineMedium)
        Text(connectionStatus, style = MaterialTheme.typography.bodyMedium)

        // Movement buttons arranged in a rhombus shape
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = { sendCommand(scope, bluetoothController, "F") },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_up),
                        contentDescription = "Move Forward"
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        onClick = { sendCommand(scope, bluetoothController, "L") },
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = "Turn Left"
                        )
                    }
                    IconButton(
                        onClick = { sendCommand(scope, bluetoothController, "S") },
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_stop),
                            contentDescription = "Stop"
                        )
                    }
                    IconButton(
                        onClick = { sendCommand(scope, bluetoothController, "R") },
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "Turn Right"
                        )
                    }
                }
                IconButton(
                    onClick = { sendCommand(scope, bluetoothController, "B") },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = "Move Backward"
                    )
                }
            }
        }

        // Other control buttons arranged in two columns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { sendCommand(scope, bluetoothController, "T") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Drop Seeds")
                }
                Button(
                    onClick = { sendCommand(scope, bluetoothController, "M") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Move Soil Sensor")
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { sendCommand(scope, bluetoothController, "P") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Activate Plow")
                }
                Button(
                    onClick = { sendCommand(scope, bluetoothController, "W") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Toggle Water Pump")
                }
            }
        }

        // Connect Bluetooth button at the bottom
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    isConnected = bluetoothController.connectToBluetooth()
                    connectionStatus = if (isConnected) "Connected to HC-05" else "Connection Failed"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Connect Bluetooth")
        }
    }
}

private fun sendCommand(scope: CoroutineScope, bluetoothController: BluetoothController, command: String) {
    scope.launch(Dispatchers.IO) {
        bluetoothController.sendCommand(command)
    }
}
package com.example.gemini

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

class BluetoothController(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    }
    private var bluetoothSocket: BluetoothSocket? = null
    private val hc05UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    suspend fun connectToBluetooth(): Boolean {
        return withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("Bluetooth", "Permissions are not granted")
                    return@withContext false
                }
            }

            val device: BluetoothDevice? = bluetoothAdapter?.bondedDevices?.find { it.name == "HC-05" }
            device?.let {
                try {
                    bluetoothSocket = it.createRfcommSocketToServiceRecord(hc05UUID)
                    bluetoothSocket?.connect()
                    Log.d("Bluetooth", "Connected to HC-05")
                    true
                } catch (e: IOException) {
                    Log.e("Bluetooth", "Connection failed", e)
                    false
                }
            } ?: false
        }
    }

    fun sendCommand(command: String): Boolean {
        return try {
            bluetoothSocket?.outputStream?.write(command.toByteArray())
            Log.d("Bluetooth", "Command sent: $command")
            true
        } catch (e: IOException) {
            Log.e("Bluetooth", "Failed to send command", e)
            false
        }
    }

    fun isConnected(): Boolean = bluetoothSocket?.isConnected == true

    fun disconnect() {
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            Log.e("Bluetooth", "Failed to close socket", e)
        }
    }
}
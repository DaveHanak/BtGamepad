package com.umpa.btgamepad

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.umpa.btgamepad.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), GamepadDevice.ConnectionStateChangeListener {
    private lateinit var binding: ActivityMainBinding

    // Permission requests:
    private var bluetoothConnectPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            setMessageSuccess(binding.lastMessage, R.string.bluetooth_permission_granted)
        } else {
            setMessageFailure(binding.lastMessage, R.string.bluetooth_permission_denied)
        }
    }
    private var bluetoothScanPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            setMessageSuccess(binding.lastMessage, R.string.bluetooth_scan_permission_granted)
        } else {
            setMessageFailure(binding.lastMessage, R.string.bluetooth_scan_permission_denied)
        }
    }
    private var bluetoothEnabledRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (isBluetoothEnabled()) {
                setMessageSuccess(binding.lastMessage, R.string.bluetooth_on)
            } else {
                setMessageFailure(binding.lastMessage, R.string.bluetooth_off)
            }
        }
    }
    private var bluetoothDiscoverableRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == 120) {
            setMessageSuccess(binding.lastMessage, R.string.device_discoverable)
        } else {
            setMessageFailure(binding.lastMessage, R.string.device_not_discoverable)
        }
    }

    // Main:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Preferences.loadPreferences(this)
        makeFullscreen(window)

        binding.startButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 31 && !hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                bluetoothConnectPermissionRequest.launch(Manifest.permission.BLUETOOTH_CONNECT)
                return@setOnClickListener
            }
            if (Build.VERSION.SDK_INT >= 31 && !hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
                bluetoothScanPermissionRequest.launch(Manifest.permission.BLUETOOTH_SCAN)
                return@setOnClickListener
            }
            if (!isBluetoothEnabled()) {
                bluetoothEnabledRequest.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                return@setOnClickListener
            }
            if (GamepadDevice.startBluetoothHidDevice(this)) {
                GamepadDevice.connectionStateChangeListener = this
                if (!isDeviceDiscoverable()) setMessageInfo(binding.lastMessage, R.string.device_enabled_nd)
                else setMessageInfo(binding.lastMessage, R.string.device_enabled_d)
                return@setOnClickListener
            }
            if (!isDeviceDiscoverable() && !GamepadDevice.isConnected) {
                bluetoothDiscoverableRequest.launch(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                return@setOnClickListener
            }
        }

        binding.enterGamepad.setOnClickListener {
            GamepadDevice.resumeBluetoothHidDevice()
            startActivity(Intent(this, GamepadActivity::class.java))
        }

        binding.stopButton.setOnClickListener {
            if (GamepadDevice.stopBluetoothHidDevice()) setMessageInfo(binding.lastMessage, R.string.device_disabled)
            else setMessageInfo(binding.lastMessage, R.string.device_off)
            forwardedConnectionStateChangeListener?.onDisconnected()
        }

        binding.enterPreferences.setOnClickListener {
            if (GamepadDevice.btHidDevice == null) startActivity(Intent(this, PreferencesActivity::class.java))
            else setMessageInfo(binding.lastMessage, R.string.preferences_block)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) { if (hasFocus) GamepadDevice.resumeBluetoothHidDevice() }

    companion object {
        var forwardedConnectionStateChangeListener: GamepadDevice.ConnectionStateChangeListener? = null
    }
    override fun onConnected() {
        GamepadDevice.saveBtDeviceAddress(this)
        GlobalScope.launch(Dispatchers.Main) {
            setMessageSuccess(binding.lastMessage, R.string.device_connected)
            forwardedConnectionStateChangeListener?.onConnected()
        }
    }
    override fun onDisconnected() {
        GlobalScope.launch(Dispatchers.Main) {
            setMessageFailure(binding.lastMessage, R.string.device_disconnected)
            forwardedConnectionStateChangeListener?.onDisconnected()
        }
    }
}
package com.umpa.btgamepad

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.BluetoothProfile.ServiceListener
import android.content.Context
import java.io.File
import java.util.concurrent.Executors

@SuppressLint("MissingPermission")
object GamepadDevice {
    private var isRegistered = false
    var isConnected = false
    interface ConnectionStateChangeListener {
        fun onConnected()
        fun onDisconnected()
    }
    var connectionStateChangeListener: ConnectionStateChangeListener? = null
    var btHidDevice: BluetoothHidDevice? = null
    var btDevice: BluetoothDevice? = null
    private const val btDeviceAddressFilename = "btDeviceAddress.pref"

    private val sdp = BluetoothHidDeviceAppSdpSettings(
        "BtGamepad",
        "BtGamepad",
        "BtGamepad",
        BluetoothHidDevice.SUBCLASS2_GAMEPAD,
        GamepadInputWrapper.getDescriptorBytes()
    )

    private val callback = object : BluetoothHidDevice.Callback() {
        override fun onAppStatusChanged(pluggedDevice: BluetoothDevice?, registered: Boolean) {
            super.onAppStatusChanged(pluggedDevice, registered)
            isRegistered = registered
            if (isRegistered && btDevice != null) {
                btHidDevice?.connect(btDevice)
            }
        }
        override fun onConnectionStateChanged(device: BluetoothDevice, state: Int) {
            super.onConnectionStateChanged(device, state)
            if (state == BluetoothProfile.STATE_CONNECTED) {
                btDevice = device
                isConnected = true
                connectionStateChangeListener?.onConnected()
            } else if (state == BluetoothProfile.STATE_DISCONNECTED) {
                isConnected = false
                connectionStateChangeListener?.onDisconnected()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun registerApp() {
        btHidDevice?.registerApp(
            sdp,
            null,
            null,
            Executors.newCachedThreadPool(),
            callback
        )
    }
    private val serviceListener = object : ServiceListener {
        @SuppressLint("MissingPermission")
        override fun onServiceConnected(profile: Int, bluetoothProfile: BluetoothProfile) {
            if (profile == BluetoothProfile.HID_DEVICE) {
                btHidDevice = bluetoothProfile as BluetoothHidDevice
                registerApp()
            }
        }
        @SuppressLint("MissingPermission")
        override fun onServiceDisconnected(profile: Int) {
            if (profile == BluetoothProfile.HID_DEVICE) {
                if (btDevice != null) {
                    btHidDevice?.disconnect(btDevice)
                }
                btHidDevice?.unregisterApp()
            }
        }
    }

    private lateinit var bluetoothAdapter: BluetoothAdapter
    @SuppressLint("MissingPermission")
    fun startBluetoothHidDevice(context: Context) : Boolean {
        return if (btHidDevice == null) {
            bluetoothAdapter = context.getBluetoothAdapter()!!
            loadBtDeviceAddress(context)
            bluetoothAdapter.getProfileProxy(
                context,
                serviceListener,
                BluetoothProfile.HID_DEVICE
            )
            GamepadReporter.startReporting()
            true
        } else false
    }
    @SuppressLint("MissingPermission")
    fun resumeBluetoothHidDevice() {
        if (!isRegistered) {
            registerApp()
        }
    }
    fun stopBluetoothHidDevice() : Boolean {
        return if (btHidDevice != null) {
            connectionStateChangeListener = null
            GamepadReporter.stopReporting()
            if (btDevice != null) {
                btHidDevice!!.disconnect(btDevice)
            }
            btHidDevice!!.unregisterApp()
            btHidDevice = null
            true
        } else false
    }

    private var rememberedAddress: String? = null
    private fun loadBtDeviceAddress(context: Context) {
        if (btDevice == null) {
            try {
                rememberedAddress = File(context.filesDir, btDeviceAddressFilename).readText()
                if (!rememberedAddress.isNullOrEmpty()) {
                    btDevice = bluetoothAdapter.getRemoteDevice(rememberedAddress)
                }
            } catch (_: Exception) {}
        }
    }
    fun saveBtDeviceAddress(context: Context) {
        if (btDevice != null) {
            if (rememberedAddress.equals(btDevice!!.address)) return
            try {
                File(context.filesDir, btDeviceAddressFilename).writeText(btDevice!!.address)
            } catch (_: Exception) {}
        }
    }
}
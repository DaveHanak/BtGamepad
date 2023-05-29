package com.umpa.btgamepad

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun makeFullscreen(window: Window) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window,
        window.decorView.findViewById(android.R.id.content)).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Context.hasPermission(permission: String) =
    checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

fun Context.getBluetoothAdapter(): BluetoothAdapter? {
    val manager = getSystemService(AppCompatActivity.BLUETOOTH_SERVICE) as BluetoothManager
    return manager.adapter ?: null
}

fun Context.isBluetoothEnabled(): Boolean {
    return getBluetoothAdapter() != null
}

@SuppressLint("MissingPermission")
fun Context.isDeviceDiscoverable(): Boolean {
    return getBluetoothAdapter()?.scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE
}

fun Context.setMessageSuccess(textView: TextView, stringId: Int) {
    textView.text = getString(stringId)
    textView.setTextColor(getColor(R.color.green_success))
}

fun Context.setMessageFailure(textView: TextView, stringId: Int) {
    textView.text = getString(stringId)
    textView.setTextColor(getColor(R.color.red_failure))
}

fun Context.setMessageInfo(textView: TextView, stringId: Int) {
    textView.text = getString(stringId)
    textView.setTextColor(getColor(R.color.yellow_info))
}

fun Context.setMessagePreferenceSet(textView: TextView, stringIdGroup: Int, stringIdChecked: Int) {
    textView.text = "Set: " + getString(stringIdGroup) + " -> " + getString(stringIdChecked)
    textView.setTextColor(getColor(R.color.yellow_info))
}
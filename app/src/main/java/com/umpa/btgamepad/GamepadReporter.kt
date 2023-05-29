package com.umpa.btgamepad

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import java.util.*

object GamepadReporter {
    private lateinit var timer: Timer
    private var isStarted = false
    private const val reportInterval: Long = 5
    fun startReporting() {
        if (!isStarted) {
            timer = Timer()
            timer.scheduleAtFixedRate(object: TimerTask() {
                override fun run() {
                    if (GamepadDevice.isConnected) {
                        sendReport(GamepadInputWrapper.createReport())
                    }
                }
            }, 0, reportInterval)
            isStarted = true
        }
    }
    fun stopReporting() {
        if (isStarted) {
            timer.cancel()
            isStarted = false
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendReport(report: GamepadReport) {
        GamepadReport.sendingState = GamepadReport.State.InProgress
        val sent = GamepadDevice.btHidDevice?.sendReport(
            GamepadDevice.btDevice,
            report.id,
            report.data
        )
        if (sent == true) {
            GamepadReport.sendingState = GamepadReport.State.Success
        } else {
            GamepadReport.sendingState = GamepadReport.State.Failure
        }
    }
}
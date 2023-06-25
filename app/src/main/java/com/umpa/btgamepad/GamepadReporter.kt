package com.umpa.btgamepad

import android.annotation.SuppressLint
import kotlin.concurrent.thread
import java.util.concurrent.TimeUnit

object GamepadReporter {
    private var isStarted = false
    fun startReporting() {
        if (!isStarted) {
            isStarted = true
            thread(priority = Thread.MAX_PRIORITY) {
                val reportIntervalNanos = Preferences.reportInterval * 1000000L
                val sleepNanos = reportIntervalNanos / 100L
                var nextSendTime = System.nanoTime() + reportIntervalNanos
                while (isStarted) {
                    if (GamepadDevice.isConnected && nextSendTime <= System.nanoTime()) {
                        nextSendTime = System.nanoTime() + reportIntervalNanos
                        sendReport(GamepadInputWrapper.createReport())
                    }
                    TimeUnit.NANOSECONDS.sleep(sleepNanos)
                }
            }
        }
    }
    fun stopReporting() {
        if (isStarted) {
            isStarted = false
        }
    }
    @SuppressLint("MissingPermission")
    private fun sendReport(report: GamepadReport) {
        GamepadDevice.btHidDevice?.sendReport(
            GamepadDevice.btDevice,
            report.id,
            report.data
        )
    }
}
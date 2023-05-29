package com.umpa.btgamepad

import android.content.Context
import android.util.Log
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

object Preferences {
    var gyroEnabled = false
    var gyroSensitivity = 1.0f
    var directModeAnalogTriggers = true
    enum class GyroReplaceable {
        LeftThumbstick, RightThumbstick
    }
    var directModeGyroReplaces = GyroReplaceable.LeftThumbstick

    private const val preferencesFilename = "btGamepad.pref"
    fun loadPreferences(context: Context) {
        try {
            val prefs = Json.decodeFromString<PreferencesSerializable>(
                File(context.filesDir, preferencesFilename).readText()
            )
            GamepadInputWrapper.changeInputMode(GamepadInputWrapper.Mode.valueOf(prefs.mode))
            gyroEnabled = prefs.gyroEnabled
            gyroSensitivity = prefs.gyroSensitivity
            directModeAnalogTriggers = prefs.analogTriggers
            directModeGyroReplaces = GyroReplaceable.valueOf(prefs.gyroReplaces)
        } catch (_: Exception) {}
    }
    fun savePreferences(context: Context) {
        try {
            val json = Json.encodeToString(
                PreferencesSerializable(
                    GamepadInputWrapper.getMode().name,
                    gyroEnabled,
                    gyroSensitivity,
                    directModeAnalogTriggers,
                    directModeGyroReplaces.name
                )
            )
            File(context.filesDir, preferencesFilename).writeText(json)
        } catch (_: Exception) {}
    }
}
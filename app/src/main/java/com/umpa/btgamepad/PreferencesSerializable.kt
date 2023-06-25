package com.umpa.btgamepad

import kotlinx.serialization.Serializable

@Serializable
data class PreferencesSerializable(
    val mode: String,
    val gyroEnabled: Boolean,
    val gyroSensitivity: Float,
    val reportInterval: Long,
    val analogTriggers: Boolean,
    val gyroReplaces: String
)
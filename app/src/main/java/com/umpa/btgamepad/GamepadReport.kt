package com.umpa.btgamepad

class GamepadReport(
    buttonFlags: Short,
    usageX: Byte,
    usageY: Byte,
    usageZ: Byte,
    usageRx: Byte,
    usageRy: Byte,
    usageRzOrHatSwitch: Byte,
    val id: Int = 0
) {
    val data: ByteArray

    init {
        val buttonFlags0to7 = buttonFlags.toByte()
        val buttonFlags8to15 = buttonFlags.toUInt().shr(8).toByte()
        data = byteArrayOf(
            buttonFlags0to7,
            buttonFlags8to15,
            usageX,
            usageY,
            usageZ,
            usageRx,
            usageRy,
            usageRzOrHatSwitch
        )
    }

    enum class State {
        Neutral, InProgress, Success, Failure
    }

    companion object {
        var sendingState = State.Neutral
    }
}

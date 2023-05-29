package com.umpa.btgamepad

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

class GamepadInputDirect : IGamepadInput {
    private var currentButtonFlags: Short = 0
    private var lxValue: Byte = 0
    private var lyValue: Byte = 0
    private var zValue: Byte = 0
    private var rxValue: Byte = 0
    private var ryValue: Byte = 0
    private var dPadDir: Byte = 0b1111

    override fun getDescriptorBytes(): ByteArray {
        return HidReportDescriptorDirect.bytes
    }

    override fun createReport(): GamepadReport {
        return GamepadReport(
            currentButtonFlags,
            lxValue,
            lyValue,
            zValue,
            rxValue,
            ryValue,
            dPadDir
        )
    }

    override fun buttonPressed(buttonId: Int) {
        val buttonFlag = (1 shl buttonId).toShort()
        currentButtonFlags = currentButtonFlags or buttonFlag
    }

    override fun buttonReleased(buttonId: Int) {
        val buttonFlag = (1 shl buttonId).toShort()
        currentButtonFlags = currentButtonFlags and buttonFlag.inv()
    }

    override fun leftThumbstickMoved(x: Int, y: Int) {
        lxValue = x.toByte()
        lyValue = y.toByte()
    }

    override fun rightThumbstickMoved(x: Int, y: Int) {
        rxValue = x.toByte()
        ryValue = y.toByte()
    }

    override fun leftTriggerPushed(x: Int) {
        zValue = if (x > Constants.logicalMaximum) Constants.logicalMaximum.toByte() else x.toByte()
    }

    override fun leftTriggerReleased() {
        zValue = 0
    }

    override fun rightTriggerPushed(x: Int) {
        zValue = if (x < Constants.logicalMinimum) Constants.logicalMinimum.toByte() else x.toByte()
    }

    override fun rightTriggerReleased() {
        zValue = 0
    }

    override fun dPadPressed(dir: String) {
        when (dir) {
            "up"         -> dPadDir = 0b000
            "up+right"   -> dPadDir = 0b001
            "right"      -> dPadDir = 0b010
            "down+right" -> dPadDir = 0b011
            "down"       -> dPadDir = 0b100
            "down+left"  -> dPadDir = 0b101
            "left"       -> dPadDir = 0b110
            "up+left"    -> dPadDir = 0b111
        }
    }

    override fun dPadReleased(dir: String) {
        dPadDir = 0b1111
    }

    override fun gyroUpdate(x: Int, y: Int) {
        if (Preferences.directModeGyroReplaces == Preferences.GyroReplaceable.LeftThumbstick) {
            leftThumbstickMoved(x, y)
        } else {
            rightThumbstickMoved(x, y)
        }
    }
}
package com.umpa.btgamepad

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

class GamepadInputRaw : IGamepadInput {
    private var currentButtonFlags: Short = 0
    private var lxValue: Byte = 0
    private var lyValue: Byte = 0
    private var rxValue: Byte = 0
    private var ryValue: Byte = 0
    private var gxValue: Byte = 0
    private var gyValue: Byte = 0

    private val leftTriggerId = 10
    private val rightTriggerId = 11
    private val dpadUpId = 12
    private val dpadDownId = 13
    private val dpadLeftId = 14
    private val dpadRightId = 15

    override fun getDescriptorBytes(): ByteArray {
        return HidReportDescriptorRaw.bytes
    }

    override fun createReport(): GamepadReport {
        return GamepadReport(
            currentButtonFlags,
            lxValue,
            lyValue,
            rxValue,
            ryValue,
            gxValue,
            gyValue
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
        buttonPressed(leftTriggerId)
    }

    override fun leftTriggerReleased() {
        buttonReleased(leftTriggerId)
    }

    override fun rightTriggerPushed(x: Int) {
        buttonPressed(rightTriggerId)
    }

    override fun rightTriggerReleased() {
        buttonReleased(rightTriggerId)
    }

    override fun dPadPressed(dir: String) {
        when (dir) {
            "up"         -> buttonPressed(dpadUpId)
            "down"       -> buttonPressed(dpadDownId)
            "left"       -> buttonPressed(dpadLeftId)
            "right"      -> buttonPressed(dpadRightId)
            "up+left"    -> {buttonPressed(dpadUpId); buttonPressed(dpadLeftId)}
            "down+left"  -> {buttonPressed(dpadDownId); buttonPressed(dpadLeftId)}
            "up+right"   -> {buttonPressed(dpadUpId); buttonPressed(dpadRightId)}
            "down+right" -> {buttonPressed(dpadDownId); buttonPressed(dpadRightId)}
        }
    }

    override fun dPadReleased(dir: String) {
        when (dir) {
            "up"         -> buttonReleased(dpadUpId)
            "down"       -> buttonReleased(dpadDownId)
            "left"       -> buttonReleased(dpadLeftId)
            "right"      -> buttonReleased(dpadRightId)
            "up+left"    -> {buttonReleased(dpadUpId); buttonReleased(dpadLeftId)}
            "down+left"  -> {buttonReleased(dpadDownId); buttonReleased(dpadLeftId)}
            "up+right"   -> {buttonReleased(dpadUpId); buttonReleased(dpadRightId)}
            "down+right" -> {buttonReleased(dpadDownId); buttonReleased(dpadRightId)}
        }
    }

    override fun gyroUpdate(x: Int, y: Int) {
        gxValue = x.toByte()
        gyValue = y.toByte()
    }
}
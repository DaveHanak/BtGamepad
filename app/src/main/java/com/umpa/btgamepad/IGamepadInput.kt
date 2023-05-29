package com.umpa.btgamepad

interface IGamepadInput {
    fun getDescriptorBytes(): ByteArray
    fun createReport(): GamepadReport
    fun buttonPressed(buttonId: Int)
    fun buttonReleased(buttonId: Int)
    fun leftThumbstickMoved(x: Int, y: Int)
    fun rightThumbstickMoved(x: Int, y: Int)
    fun leftTriggerPushed(x: Int)
    fun leftTriggerReleased()
    fun rightTriggerPushed(x: Int)
    fun rightTriggerReleased()
    fun dPadPressed(dir: String)
    fun dPadReleased(dir: String)
    fun gyroUpdate(x: Int, y: Int)
}
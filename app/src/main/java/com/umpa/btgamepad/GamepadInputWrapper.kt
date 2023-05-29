package com.umpa.btgamepad

object GamepadInputWrapper : IGamepadInput {
    private var inputMode: IGamepadInput = GamepadInputDirect()

    enum class Mode {
        Direct, Raw
    }

    fun getMode(): Mode {
        return if (inputMode is GamepadInputDirect) Mode.Direct
        else Mode.Raw
    }

    fun changeInputMode(mode: Mode) {
        inputMode = if (mode == Mode.Direct) {
            GamepadInputDirect()
        } else {
            GamepadInputRaw()
        }
    }

    override fun getDescriptorBytes(): ByteArray {
        return inputMode.getDescriptorBytes()
    }

    override fun createReport(): GamepadReport {
        return inputMode.createReport()
    }

    override fun buttonPressed(buttonId: Int) {
        inputMode.buttonPressed(buttonId)
    }

    override fun buttonReleased(buttonId: Int) {
        inputMode.buttonReleased(buttonId)
    }

    override fun leftThumbstickMoved(x: Int, y: Int) {
        inputMode.leftThumbstickMoved(x, y)
    }

    override fun rightThumbstickMoved(x: Int, y: Int) {
        inputMode.rightThumbstickMoved(x, y)
    }

    override fun leftTriggerPushed(x: Int) {
        inputMode.leftTriggerPushed(x)
    }

    override fun leftTriggerReleased() {
        inputMode.leftTriggerReleased()
    }

    override fun rightTriggerPushed(x: Int) {
        inputMode.rightTriggerPushed(x)
    }

    override fun rightTriggerReleased() {
        inputMode.rightTriggerReleased()
    }

    override fun dPadPressed(dir: String) {
        inputMode.dPadPressed(dir)
    }

    override fun dPadReleased(dir: String) {
        inputMode.dPadReleased(dir)
    }

    override fun gyroUpdate(x: Int, y: Int) {
        inputMode.gyroUpdate(x, y)
    }
}
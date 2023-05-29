package com.umpa.btgamepad

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import com.umpa.btgamepad.Thumbstick.ThumbstickListener
import com.umpa.btgamepad.databinding.ActivityGamepadBinding

class GamepadActivity : AppCompatActivity(), ThumbstickListener, SensorEventListener, GamepadDevice.ConnectionStateChangeListener {
    private lateinit var binding: ActivityGamepadBinding

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamepadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (GamepadDevice.isConnected) {
            setMessageSuccess(binding.forwardedMessage, R.string.device_connected)
        } else {
            setMessageFailure(binding.forwardedMessage, R.string.device_disconnected)
        }
        MainActivity.forwardedConnectionStateChangeListener = this
        makeFullscreen(window)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)

        val buttonOtl = OnTouchListener {v, event->
            if (event.action == MotionEvent.ACTION_DOWN) {
                GamepadInputWrapper.buttonPressed(v.tag.toString().toInt())
            } else if (event.action == MotionEvent.ACTION_UP) {
                GamepadInputWrapper.buttonReleased(v.tag.toString().toInt())
            }
            false
        }

        binding.aButton.setOnTouchListener(buttonOtl)
        binding.bButton.setOnTouchListener(buttonOtl)
        binding.xButton.setOnTouchListener(buttonOtl)
        binding.yButton.setOnTouchListener(buttonOtl)
        binding.leftButton.setOnTouchListener(buttonOtl)
        binding.rightButton.setOnTouchListener(buttonOtl)
        binding.backButton.setOnTouchListener(buttonOtl)
        binding.startButton.setOnTouchListener(buttonOtl)

        val dpadOtl = OnTouchListener {v, event->
            if (event.action == MotionEvent.ACTION_DOWN) {
                GamepadInputWrapper.dPadPressed(v.tag.toString())
            } else if (event.action == MotionEvent.ACTION_UP) {
                GamepadInputWrapper.dPadReleased(v.tag.toString())
            }
            false
        }

        binding.nDpad.setOnTouchListener(dpadOtl)
        binding.neDpad.setOnTouchListener(dpadOtl)
        binding.eDpad.setOnTouchListener(dpadOtl)
        binding.seDpad.setOnTouchListener(dpadOtl)
        binding.sDpad.setOnTouchListener(dpadOtl)
        binding.swDpad.setOnTouchListener(dpadOtl)
        binding.wDpad.setOnTouchListener(dpadOtl)
        binding.nwDpad.setOnTouchListener(dpadOtl)

        val leftTriggerOtl = if (Preferences.directModeAnalogTriggers) OnTouchListener {v, event->
            if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
                GamepadInputWrapper.leftTriggerPushed((Constants.logicalMaximum * event.x / v.width).toInt())
            } else if (event.action == MotionEvent.ACTION_UP) {
                GamepadInputWrapper.leftTriggerReleased()
            }
            false
        } else OnTouchListener {_, event->
            if (event.action == MotionEvent.ACTION_DOWN) {
                GamepadInputWrapper.leftTriggerPushed(Constants.logicalMaximum)
            } else if (event.action == MotionEvent.ACTION_UP) {
                GamepadInputWrapper.leftTriggerReleased()
            }
            false
        }

        val rightTriggerOtl = if (Preferences.directModeAnalogTriggers) OnTouchListener {v, event->
            if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_DOWN) {
                GamepadInputWrapper.rightTriggerPushed((Constants.logicalMinimum * (v.width - event.x) / v.width).toInt())
            } else if (event.action == MotionEvent.ACTION_UP) {
                GamepadInputWrapper.rightTriggerReleased()
            }
            false
        } else OnTouchListener {_, event->
            if (event.action == MotionEvent.ACTION_DOWN) {
                GamepadInputWrapper.rightTriggerPushed(Constants.logicalMinimum)
            } else if (event.action == MotionEvent.ACTION_UP) {
                GamepadInputWrapper.rightTriggerReleased()
            }
            false
        }

        binding.leftTrigger.setOnTouchListener(leftTriggerOtl)
        binding.rightTrigger.setOnTouchListener(rightTriggerOtl)

        binding.leftThumbstick.listener = this
        binding.rightThumbstick.listener = this
    }

    override fun onMove(thumbstick: Thumbstick?, x: Int, y: Int) {
        if (GamepadInputWrapper.getMode() == GamepadInputWrapper.Mode.Direct && Preferences.gyroEnabled) {
            if (Preferences.directModeGyroReplaces == Preferences.GyroReplaceable.RightThumbstick) {
                GamepadInputWrapper.leftThumbstickMoved(x, y)
            } else {
                GamepadInputWrapper.rightThumbstickMoved(x, y)
            }
        } else {
            if (thumbstick === binding.leftThumbstick) {
                GamepadInputWrapper.leftThumbstickMoved(x, y)
            } else if (thumbstick === binding.rightThumbstick) {
                GamepadInputWrapper.rightThumbstickMoved(x, y)
            }
        }
    }

    override fun onDoubleTapPressed(thumbstick: Thumbstick?) {
        GamepadInputWrapper.buttonPressed(thumbstick?.tag.toString().toInt())
    }

    override fun onDoubleTapReleased(thumbstick: Thumbstick?) {
        GamepadInputWrapper.buttonReleased(thumbstick?.tag.toString().toInt())
    }


    private val rotationMatrix = FloatArray(9)
    private val orientation = FloatArray(3)
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            SensorManager.getOrientation(rotationMatrix, orientation)

            var rotX = -(Constants.logicalMaximum * orientation[1] * Preferences.gyroSensitivity).toInt()
            var rotY = -(Constants.logicalMaximum * orientation[2] * Preferences.gyroSensitivity).toInt()

            if (rotX > Constants.logicalMaximum) rotX = Constants.logicalMaximum
            else if (rotX < Constants.logicalMinimum) rotX = Constants.logicalMinimum
            if (rotY > Constants.logicalMaximum) rotY = Constants.logicalMaximum
            else if (rotY < Constants.logicalMinimum) rotY = Constants.logicalMinimum

            GamepadInputWrapper.gyroUpdate(rotX, rotY)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onResume() {
        super.onResume()
        if (Preferences.gyroEnabled) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        if (Preferences.gyroEnabled) {
            sensorManager.unregisterListener(this)
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) { if (hasFocus) GamepadDevice.resumeBluetoothHidDevice() }

    override fun onConnected() {
        setMessageSuccess(binding.forwardedMessage, R.string.device_connected)
    }
    override fun onDisconnected() {
        setMessageFailure(binding.forwardedMessage, R.string.device_disconnected)
    }
}
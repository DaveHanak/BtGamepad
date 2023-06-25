package com.umpa.btgamepad

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.umpa.btgamepad.databinding.ActivityPreferencesBinding

class PreferencesActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private lateinit var binding: ActivityPreferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeFullscreen(window)

        binding.sensitivityBar.setOnSeekBarChangeListener(this)
        binding.intervalBar.setOnSeekBarChangeListener(this)

        binding.rModeDir.isChecked = GamepadInputWrapper.getMode() == GamepadInputWrapper.Mode.Direct
        binding.rModeRaw.isChecked = GamepadInputWrapper.getMode() == GamepadInputWrapper.Mode.Raw

        binding.rTrigAnalog.isChecked = Preferences.directModeAnalogTriggers
        binding.rTrigDigital.isChecked = !Preferences.directModeAnalogTriggers

        binding.rGyroOn.isChecked = Preferences.gyroEnabled
        binding.rGyroOff.isChecked = !Preferences.gyroEnabled

        binding.rGyroLeft.isChecked = Preferences.directModeGyroReplaces == Preferences.GyroReplaceable.LeftThumbstick
        binding.rGyroRight.isChecked = Preferences.directModeGyroReplaces == Preferences.GyroReplaceable.RightThumbstick

        binding.sensitivityBar.progress = (Preferences.gyroSensitivity * 100).toInt()
        binding.sensitivityCounter.text = binding.sensitivityBar.progress.toString()

        binding.intervalBar.progress = Preferences.reportInterval.toInt()
        binding.intervalCounter.text = getString(R.string.ms, binding.intervalBar.progress)

        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        Preferences.savePreferences(this)
    }

    fun onModeRadioButtonClicked(view: View) {
        if (view is RadioButton && view.isChecked) {
            when (view.getId()) {
                R.id.rModeDir -> {
                    GamepadInputWrapper.changeInputMode(GamepadInputWrapper.Mode.Direct)
                    setMessagePreferenceSet(binding.lastMessage, R.string.pref_mode, R.string.pref_mode_dir)
                }
                R.id.rModeRaw -> {
                    GamepadInputWrapper.changeInputMode(GamepadInputWrapper.Mode.Raw)
                    setMessagePreferenceSet(binding.lastMessage, R.string.pref_mode, R.string.pref_mode_raw)
                }
            }
        }
    }

    fun onTrigRadioButtonClicked(view: View) {
        if (view is RadioButton && view.isChecked) {
            when (view.getId()) {
                R.id.rTrigAnalog -> {
                    Preferences.directModeAnalogTriggers = true
                    setMessagePreferenceSet(binding.lastMessage, R.string.pref_trig, R.string.pref_trig_a)
                }
                R.id.rTrigDigital -> {
                    Preferences.directModeAnalogTriggers = false
                    setMessagePreferenceSet(binding.lastMessage, R.string.pref_trig, R.string.pref_trig_d)
                }
            }
        }
    }

    fun onGyroRadioButtonClicked(view: View) {
        if (view is RadioButton && view.isChecked) {
            when (view.getId()) {
                R.id.rGyroOn -> {
                    Preferences.gyroEnabled = true
                    setMessagePreferenceSet(binding.lastMessage, R.string.pref_gyro, R.string.pref_on)
                }
                R.id.rGyroOff -> {
                    Preferences.gyroEnabled = false
                    setMessagePreferenceSet(binding.lastMessage, R.string.pref_gyro, R.string.pref_off)
                }
            }
        }
    }

    fun onGyroReplaceRadioButtonClicked(view: View) {
        if (view is RadioButton && view.isChecked) {
            when (view.getId()) {
                R.id.rGyroLeft -> {
                    Preferences.directModeGyroReplaces = Preferences.GyroReplaceable.LeftThumbstick
                    setMessagePreferenceSet(binding.lastMessage, R.string.pref_side, R.string.pref_side_l)
                }
                R.id.rGyroRight -> {
                    Preferences.directModeGyroReplaces = Preferences.GyroReplaceable.RightThumbstick
                    setMessagePreferenceSet(binding.lastMessage, R.string.pref_side, R.string.pref_side_r)
                }
            }
        }
    }

    override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekbar != null) {
            if (seekbar === binding.sensitivityBar) {
                binding.sensitivityCounter.text = progress.toString()
            } else if (seekbar === binding.intervalBar) {
                binding.intervalCounter.text = getString(R.string.ms, progress)
            }
        }
    }

    override fun onStartTrackingTouch(seekbar: SeekBar?) {
        if (seekbar != null) {
            if (seekbar === binding.sensitivityBar) {
                binding.sensitivityCounter.setTextColor(getColor(R.color.yellow_info))
            } else if (seekbar === binding.intervalBar) {
                binding.intervalCounter.setTextColor(getColor(R.color.yellow_info))
            }
        }
    }

    override fun onStopTrackingTouch(seekbar: SeekBar?) {
        if (seekbar != null) {
            if (seekbar === binding.sensitivityBar) {
                Preferences.gyroSensitivity = seekbar.progress.toFloat() / 100
                binding.sensitivityCounter.setTextColor(getColor(R.color.white))
            } else if (seekbar === binding.intervalBar) {
                Preferences.reportInterval = seekbar.progress.toLong()
                binding.intervalCounter.setTextColor(getColor(R.color.white))
            }
        }
    }
}
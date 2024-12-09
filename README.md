# BtGamepad

BtGamepad is an application for devices running Android version 9 and above. It essentially turns your phone into a universal HID gamepad device, communicating via Bluetooth. In addition to standard buttons and analog sticks, the gamepad also features a gyroscope function and allows you to choose the input mode: one compatible with the DirectInput API, typically for Windows, or a universal mode for applications with custom APIs for reading HID-compliant gamepad inputs.

## Why does this even exist

Originally, it was made as a university project with future development plans possibly taking it to the App Store. What stopped me in my tracks was the fact that mobile phones' Bluetooth modules aren't really up to the task of emulating a HID gamepad. You'll see for yourself that your ability to use the gamepad will be greatly affected by the quality of the signal, which is prone to a lot of interference from surrounding devices. Sending the HID reports in real time thus becomes a challenge impossible to overcome for your standard Android phone, resulting in delayed movements in the game, and your character falling off a cliff and dying horribly. Or maybe I'm just doing something wrong and it's entirely my own programming's fault - please let me know somehow if you manage to play something more action packed than strategy games with BtGamepad.

## How to use

To start using the gamepad, press the START button in the Menu view. If the application lacks certain permissions, pressing the button will first prompt the user to grant them. By following the on-screen instructions, the user will eventually be asked to connect to the device where the gamepad will be used. Once successfully connected, you can press the GAMEPAD button and proceed to the control view. The last successful gamepad connection is remembered so that after restarting the app and pressing START, the gamepad can resume operation seamlessly. With the gamepad active, pressing STOP pauses its operation. Accessing the Preferences view is only possible when the gamepad is stopped and can be done by pressing the PREFERENCES button in the Menu. The Preferences view allows customization of the gamepad’s functionality to suit the user’s needs or the game that will use it.

## Available options:

- Input mode: DirectInput or Raw (universal),
- Analog or digital triggers in DirectInput mode (the pressure on analog triggers changes based on the position where the trigger is pressed; the closer to the edge of the LT or RT button nearest the center of the screen, the greater the pressure),
- Gyroscope function (in DirectInput, the gyroscope replaces one of the analog sticks, while in Raw mode, it adds 2 additional axes),
- Choice of analog stick to be replaced by the gyroscope in DirectInput mode,
- Gyroscope sensitivity,
- Time interval between sent HID reports.

## Screenshots

![Bt1](https://github.com/DaveHanak/UMPAnUMiW-Projekt-BtGamepad/assets/72354597/b67ddbac-baa5-4b66-afb3-b0cd2c7af3dd)
![Bt2](https://github.com/DaveHanak/UMPAnUMiW-Projekt-BtGamepad/assets/72354597/cb4532f3-8272-4137-a96f-83a1c42d8907)
![Bt3](https://github.com/DaveHanak/UMPAnUMiW-Projekt-BtGamepad/assets/72354597/c26b7e69-254c-431a-8b82-7b4a12ed76b8)

# Lumos
![Collage 1](/screenshots/Collage1.png)
![Collage 2](/screenshots/Collage2.png)

Android part of the Lumos LED strip project, a unique and beautiful DIY Bluethooth-controlled garland based on the WS2812B addressable LED strip and HC-06 UART Bluetooth module.

## Goals for this project
* Explore the capabilities of communication between Android and Arduino platforms using Bluetooth;
* Build a unique DIY garland with various interesting effects;
* Practice controlling hardware components on the Android platform;
* Practice programming microcontrollers (currently the ATMega328P), data exchange and parsing, and creating state machines.

## TODO
- [x] Implement app permission handling;
- [x] Add effects;
- [ ] Add more controls to different garland effects;
- [ ] Improve communication protocol:
  - [ ] define a fixed command size;
  - [ ] calculate the command checksum and attach it to the data frame;
  - [ ] obtain transmission status information (feedback from the Arduino after each transmitted command).

## Known issues (Android app)
* The saturation and value selection from the dedicated color panel is laggy for now. To be fixed in future versions.

## Special thanks
[Abhilash](https://github.com/V-Abhilash-1999) for the idea of the color picker implementation in Jetpack Compose.

[Alex Samokhvalov](https://lottiefiles.com/alex_motion) for the disabled [Bluetooth](https://lottiefiles.com/animations/professional-icon-animation-pYejkhGHZu) state animation.<br/>
[Hamza Er](https://lottiefiles.com/xwlv6bikbz) for the [empty pairing list](https://lottiefiles.com/animations/connect-icon-W2s7wnF5Sw) animation.<br/>
[Elias Larsson](https://lottiefiles.com/fw3mwoig1ro9zs5t) for the [garland](https://lottiefiles.com/animations/girland-0OYnKZg21I) loading animation.

## See also
The [Arduino](https://github.com/andrew-andrushchenko/Lumos-arduino) part of the project.

If you are interested in this project, I will appreciate any help or ideas. Feel free to ask or suggest something.

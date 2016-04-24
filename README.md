# swearc
Altens contribution to http://www.swearc.se/

## Installing dependencies

### MinGW for Windows
Install MinGW from https://sourceforge.net/projects/mingw/files/

Install atleast the following packages:
* mingw32-base
* mingw32-gcc-g++
* mingw32-pthreads-w32

The installation have only been tested to c:/MinGW

### Arduino on Windows
Install to C:/Arduino, since "C:/Program Files" creates additional problems.

### Android on Windows
Set you ANDROID_HOME environment variable to point to the sdk folder of your installation.

## Building

To build everything, just type:

  ./gradlew build

If you just want to build a subproject, you may specify it as so:

  ./gradlew arduino:main:build
  ./gradlew android:main:build

## Sub projects

* [Arduino main](arduino/)
* [Android main app](android/main/app/)
* [Log web server](android/webserver/)
* [Log web server DEMO](android/webserver-demo/)

#!/bin/bash
orgDir=`pwd`
cd "$(dirname "$0")"
antimicrox-3.5.1-PortableWindows-AMD64/bin/antimicrox.exe --profile video-launcher_joystick.gamecontroller.amgp&
cd $orgDir

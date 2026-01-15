#!/bin/bash
#path="$1"
path=`pwd`"/plugin-projects/SeleniumPython/"


cd "$(dirname "$0")"
cp "$path"GrabFolder/Roku/*.url .
cp "$path"GrabFolder/Roku/images/*.png images/

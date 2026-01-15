#!/bin/bash
#path="$1"
path=`pwd`"/plugin-projects/SeleniumPython/"
cd "$(dirname "$0")"
rm *.url
cd $path
rm GrabFolder/Roku/*.url
start "rokuCategoryGrab.cmd"

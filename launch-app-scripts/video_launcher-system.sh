#!/bin/bash
cd "$(dirname "$0")"
cd ..
java -jar Application\ Builder.jar "Properties/data/Video Launcher-system.xml" > /dev/null 2>&1

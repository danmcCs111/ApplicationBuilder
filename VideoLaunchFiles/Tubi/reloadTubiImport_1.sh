#!/bin/bash
cd "$(dirname "$0")"

rm *.url images/*.png
./importTubi.sh "$1"
./importTubiImages.sh "$1"

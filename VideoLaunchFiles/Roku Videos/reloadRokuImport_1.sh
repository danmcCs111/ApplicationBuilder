#!/bin/bash
cd "$(dirname "$0")"

rm *.url images/*.png
./importRoku.sh "$1"
./importRokuImages.sh "$1"

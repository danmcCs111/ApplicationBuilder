#!/bin/bash
cd "$(dirname "$0")"

rm *.url
./importTubi.sh "$1"
./importRetryTubiImages_2.sh "$1"

read -p "press enter to close"

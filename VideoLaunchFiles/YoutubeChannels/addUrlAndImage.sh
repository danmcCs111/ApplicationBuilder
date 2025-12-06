#!/bin/bash
cd "$(dirname "$0")"

url=$1
title=$2
imageFileName=$title.png
urlFileName=$title.url
image_url=$3

echo $1
echo $2
echo $3

echo [InternetShortcut] > $urlFileName
echo "URL="$url >> $urlFileName

curl --output "images/$imageFileName" $image_url

#!/bin/bash
url=$1
fileName=`echo $url | egrep -o "/[^\/.*]*$" | sed 's/\///g'`.url
echo $fileName
echo [InternetShortcut] > $fileName
echo "URL="$url >> $fileName

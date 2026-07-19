#!/bin/bash
cd "$(dirname "$0")"

typeOs=`uname`
autoHotKey=$1
autoHotKey_file=$2
dirPath=$3
scrapeFilename=$4
dirPathLinux=$(realpath "$dirPath")

if [[ "$typeOs" == "Linux" ]]
then
	dirPath=$dirPathLinux
else
	dirPath=`echo $(realpath "$dirPath") | sed 's/^\///g' | sed -E 's/^(.{1})/\1:/g'`
fi

projLocation=$dirPathLinux

"$autoHotKey" "$autoHotKey_file" "$dirpath" "$scrapeFilename"
#!/bin/bash

orgDir=`pwd`
cd "$(dirname "$0")"
typeOs=`uname`

if [[ "$typeOs" == "Linux" ]]
then

	curl https://sourceforge.net/projects/launch4j/files/launch4j-3/3.50/launch4j-3.50-linux-x64.tgz/download --output launch4j-3.50-linux-x64.tgz
else
	curl https://sourceforge.net/projects/launch4j/files/launch4j-3/3.50/launch4j-3.50-win32.exe/download --output launch4j-3.50-win32.exe
fi

cd $orgDir

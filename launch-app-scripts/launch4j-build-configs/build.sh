#!/bin/bash

orgDir=`pwd`
cd "$(dirname "$0")"
typeOs=`uname`

#adjust if needed.
linuxCom=""
windowsCom="/c/Program Files (x86)/Launch4j/launch4j.exe"

if [[ "$typeOs" == "Linux" ]]
then
	com=linuxCom
else
	com=windowsCom
fi

builds=(`ls *.xml`)

for f in ${builds[@]}
do
	"$com" `pwd`/$f
done

cd $orgDir

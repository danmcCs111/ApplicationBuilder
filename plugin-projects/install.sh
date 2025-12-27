#!/bin/bash

projects=("https://github.com/danmcCs111/SeleniumPython.git" "https://github.com/danmcCs111/PlayrightCopyUrl.git" "https://github.com/danmcCs111/AutoHotKey-Utils.git")

for proj in ${projects[@]}
do
	projFolder=$(echo $proj | egrep -o "[^\/]*.git" | sed 's/.git//g')
	exists=`ls | grep $projFolder`

	echo "checking -> " $projFolder 
	if [[ -z "$exists" ]]
	then
		echo "installing -> " $projFolder 
		git clone $proj
	fi
done


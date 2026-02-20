#!/bin/bash

source ./packages.sh

for proj in ${projects[@]}
do
	projFolder=$(echo $proj | egrep -o "[^\/]*.git" | sed 's/.git//g')
	cd $projFolder
	git pull
	cd ..
done


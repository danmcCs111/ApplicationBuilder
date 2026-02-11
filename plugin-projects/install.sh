#!/bin/bash

source ./packages.sh

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

	maven=`find $projFolder -name maven-build.sh`
	if [[ -z "$maven" ]]
	then
		echo "no maven"
	else
		echo $maven
		./$maven
	fi

	installFolder=$projFolder"/install/install.sh"
	ls $installFolder
	$installFolder
done


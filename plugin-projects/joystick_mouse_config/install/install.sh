#!/bin/bash
orgDir=`pwd`
cd "$(dirname "$0")"
cd ..
dir="antimicrox-3.5.1-PortableWindows-AMD64"
if [[ -d "$dir" ]]
then
	echo "antimicrox exists"
else
	curl https://github.com/AntiMicroX/antimicrox/releases/download/3.5.1/antimicrox-3.5.1-PortableWindows-AMD64.zip -L --output antimicrox-3.5.1-PortableWindows-AMD64.zip 
	unzip "antimicrox-3.5.1-PortableWindows-AMD64.zip"
	curl https://github.com/AntiMicroX/antimicrox/releases/download/3.5.1/antimicrox-3.5.1-ubuntu-24.04-x86_64.deb -L --output antimicrox-3.5.1-ubuntu-24.04-x86_64.deb
fi

cd $orgDir
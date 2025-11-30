#!/bin/bash
cd "$(dirname "$0")"

scriptDirectory="$1"
rokuDirectory=`pwd`/images/

scriptName="/GrabScripts/rokuReprocessPosterArt.sh"
rokuPngs="/GrabFolder/Roku/images/*.png"

script=$scriptDirectory$scriptName

$script
cp $scriptDirectory$rokuPngs $rokuDirectory

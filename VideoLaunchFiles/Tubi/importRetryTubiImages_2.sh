#!/bin/bash
cd "$(dirname "$0")"

scriptDirectory="$1"
tubiDirectory=`pwd`/images/

scriptName="/GrabScripts/Tubi/tubiReprocessPosterArt.sh"
tubiPngs="/GrabFolder/Tubi/images/*.png"

script=$scriptDirectory$scriptName

$script
cp $scriptDirectory$tubiPngs $tubiDirectory

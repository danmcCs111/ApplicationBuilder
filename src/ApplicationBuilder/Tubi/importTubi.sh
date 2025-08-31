#!/bin/bash
scriptDirectory="$1"
tubiDirectory=`pwd`

scriptName="/GrabScripts/collectAllTubiUrls.sh"
tubiFolder="/GrabFolder/Tubi/*.url"

script=$scriptDirectory$scriptName

$script
cp $script$tubiFolder $tubiDirectory


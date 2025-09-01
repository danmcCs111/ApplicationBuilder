#!/bin/bash
cd "$(dirname "$0")"

scriptDirectory="$1"
tubiDirectory=`pwd`

scriptName="/GrabScripts/collectAllTubiUrls.sh"
tubiUrls="/GrabFolder/Tubi/*.url"

script=$scriptDirectory$scriptName

$script
cp $scriptDirectory$tubiUrls $tubiDirectory

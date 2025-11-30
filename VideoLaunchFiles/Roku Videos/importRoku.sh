#!/bin/bash
cd "$(dirname "$0")"

scriptDirectory="$1"
rokuDirectory=`pwd`

scriptName="/GrabScripts/collectAllRokuUrls.sh"
rokuUrls="/GrabFolder/Roku/*.url"

script=$scriptDirectory$scriptName

$script
cp $scriptDirectory$rokuUrls $rokuDirectory

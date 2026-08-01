#!/bin/bash

file=$1
valuesCount=`wc -l $file | awk '{print $1}'`

for i in $(eval echo "{1..$valuesCount}")
do
	echo $i
	val=`sed -n "${i},${i}p" $file`
	echo $val
	valWeb=`echo $val | sed 's/ /+/g'`
	../../plugin-projects/AutoHotKey-Utils/install/v2/AutoHotkey64.exe ../../plugin-projects/AutoHotKey-Utils/scrape.ahk "$val" "https://www.google.com/search?q=site:+youtube.com+news+youtube+channel+$valWeb"
done


#!/bin/bash
#
file=$1
valuesCount=`wc -l $file | awk '{print $1}'`

for i in $(eval echo "{1..$valuesCount}")
do
	echo $i
	val=`sed -n "${i},${i}p" $file`
	echo $val
	valFle=`echo $val | sed 's/ /*/g'`
	./pageParse.sh ../../plugin-projects/AutoHotKey-Utils/*channel\ $valFle*.html "$val"
done

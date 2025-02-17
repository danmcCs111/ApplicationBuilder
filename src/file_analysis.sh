#!/bin/bash
#print line count of each file.

defaultFilter=.java

if [ $# -eq 0 ]
then
	argFile=$defaultFilter
	echo "apply filter: " $argFile " (pass argument for another file filter)"
else
	argFile=$1
	echo "apply filter: " $argFile
fi

vals=`find | grep $argFile | awk '{system("wc -l " $NF)}' | sort -n | awk '{system("echo \"" $1 $2 "\"")}'`
total=0
for v in ${vals[@]}
do
	linecount=`echo $v | egrep -o "^[0-9]*"`
	total=$((total + linecount))
	filename=`echo $v | sed "s/$linecount//g"`
	echo $filename " | Line Count - " $linecount " | Running Total - " $total
done
echo "total line count: " $total

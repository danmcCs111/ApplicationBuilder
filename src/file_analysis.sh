#!/bin/bash

#print line count of each file.

if [ $# -eq 0 ]
then
	argFile=.java
	echo "apply filter: " $argFile " (pass argument to change)"
else
	argFile=$1
	echo "apply filter: " $argFile
fi

find | grep $argFile | awk '{system(" wc -l " $NF)}' | sort -n 

vals=`find | grep $argFile | awk '{system("wc -l " $NF)}' | sort -n | awk '{print $1}'`
total=0
for v in ${vals[@]}
do
	total=$((total + v))
done
echo "total line count: " $total

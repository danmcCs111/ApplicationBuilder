#!/bin/bash
#print line count of each file.

defaultFilter=.java
pad="-"
padLength=60

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
	padding=""
	linecount=`echo $v | egrep -o "^[0-9]*"`
	total=$((total + linecount))
	filename=`echo $v | sed "s/$linecount//g"`
	fLen=$(expr length $filename)
	len=$(( padLength - fLen ))
	padding=`for i in $(seq 1 $len); do echo -n $pad; done`
	echo $filename $padding " | Line Count - " $linecount " | Running Total - " $total
done
echo "total line count: " $total

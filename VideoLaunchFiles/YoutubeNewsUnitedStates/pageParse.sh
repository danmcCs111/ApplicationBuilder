#!/bin/bash
orgDir=`pwd`
cd "$(dirname "$0")"

file=$1
name=$2

delim="@,@"

url=(`egrep -o "https://www.youtube.com/[^\"]+" "$file" | sort -n | uniq | grep -v ";" | grep -v "watch?" | grep -v "shorts/"`)
for u in ${url[@]}
do
	curl -s $u/videos --output tmp.txt

	link=`cat tmp.txt | egrep -o originalUrl\":\"[^\"]+ | sed 's/originalUrl\":\"//g'`
	handle=`cat tmp.txt | egrep -o canonicalBaseUrl\":\"/[^\"/]+ | sed 's/canonicalBaseUrl\":\"\///g' | sort -n | uniq | tail -1`
	img=(`cat tmp.txt | egrep -o "https://yt3.googleusercontent.com([^\"])+"`)
	title=`cat tmp.txt | egrep -o "<title>([^<])+" | sed 's/<title>//g'`

	echo $title$delim$link$delim$handle$delim${img[0]} >> "$name".txt
done

rm tmp.txt
cd $orgDir

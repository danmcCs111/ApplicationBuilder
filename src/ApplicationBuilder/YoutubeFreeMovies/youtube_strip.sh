#!/bin/bash
rm *.url
cat youtube_free.txt | sed 's/\"/\n/g' | sed 's/:/ /g' | egrep -o "(watch\?.*)" > watch.txt
cat youtube_free.txt | sed 's/\"/\n/g' | sed 's/:/ /g' | egrep -A6 "title" | grep -A3 text | egrep ^[0-9A-Z]+ > you_name.txt
bottomStripCount=8
watchLen=`cat watch.txt | wc -l`
len=`cat you_name.txt | wc -l`
topStripCount=$(( len - bottomStripCount - watchLen ))
cat you_name.txt | sed -n "$(( topStripCount + 1 )),$(( len - bottomStripCount ))p" > you_name2.txt

for i in $(seq 1 $watchLen) 
do 
	filename=$(cat you_name2.txt | sed -n "$(( i )),$(( i ))p")
	filename=$filename".url"
	echo "URL=https://youtube.com/"`cat watch.txt | sed -n "$(( i )),$(( i ))p"` > "$filename"
done
rm watch.txt you_name.txt you_name2.txt

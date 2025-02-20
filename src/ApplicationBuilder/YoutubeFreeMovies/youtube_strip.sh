#!/bin/bash
#todo use array
rm *.url
rm images/*.url

cat youtube_free.txt | sed 's/\"/\n/g' | sed 's/:/ /g' | egrep -o "(watch\?.*)" > watch.txt
cat youtube_free.txt | sed 's/"/\n/g' | egrep -o "(ytimg.*)" > img.txt
cat youtube_free.txt | sed 's/\"/\n/g' | sed 's/:/ /g' | egrep -A6 "title" | grep -A3 text | egrep ^[0-9A-Z]+ > you_name.txt

watchPrefix="watch?v="
#watchPostfix="\\u0026"
watchPostfix="\u0026.*"
backslash="\\\\"
bottomStripCount=8
watchLen=`cat watch.txt | wc -l`
len=`cat you_name.txt | wc -l`
topStripCount=$(( len - bottomStripCount - watchLen ))

cat you_name.txt | sed -n "$(( topStripCount + 1 )),$(( len - bottomStripCount ))p" > you_name2.txt

for i in $(seq 1 $watchLen) 
do
	filename=$(cat you_name2.txt | sed -n "$(( i )),$(( i ))p")
	filenameImg="images/"$filename"_img.url"
	filenameVid=$filename".url"
	vidId="`cat watch.txt | sed -n "$(( i )),$(( i ))p" | sed "s/$watchPrefix//g" | sed "s/$watchPostfix//g" | sed "s/$backslash//g" `"

	echo "$vidId"
	echo `cat watch.txt | grep "$vidId" watch.txt`

	echo "[InternetShortcut]" > "$filenameVid"
	echo "URL=https://youtube.com/"`cat watch.txt | sed -n "$(( i )),$(( i ))p"` >> "$filenameVid"

	echo "[InternetShortcut]" > "$filenameImg"
	echo "URL=https://i."`cat img.txt | grep "$vidId"` >> "$filenameImg"
done
rm watch.txt you_name.txt you_name2.txt img.txt

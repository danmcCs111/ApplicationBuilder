#!/bin/bash

if [ -z $1 ];
then
	echo "enter filename"
	exit
fi

youtube_file=$1
echo $youtube_file

cat $youtube_file | sed 's/\"/\n/g' | sed 's/:/ /g' | egrep -o "(watch\?.*)" > watch.txt
cat $youtube_file | sed 's/"/\n/g' | egrep -o "(ytimg.*)" > img.txt
cat $youtube_file | sed 's/\"/\n/g' | sed 's/:/ /g' | egrep -A6 "title" | grep -A3 text | egrep ^[0-9A-Z]+ > you_name.txt

watchPrefix="watch?v="
watchPostfix="\u0026.*"
backslash="\\\\"
bottomStripCount=8
watchLen=`cat watch.txt | wc -l`
len=`cat you_name.txt | wc -l`
topStripCount=$(( len - bottomStripCount - watchLen ))

cat you_name.txt | sed -n "$(( topStripCount + 1 )),$(( len - bottomStripCount ))p" > you_name2.txt

for i in $(seq 1 $watchLen) 
do
	
	filename_tmp=$(cat you_name2.txt | sed -n "$(( i )),$(( i ))p")
	#filenameImg="images/"$filename_tmp"_img.url"
	filenameImg="images/"$filename_tmp".png"
	filenameVid=$filename_tmp".url"
	vidId="`cat watch.txt | sed -n "$(( i )),$(( i ))p" | sed "s/$watchPrefix//g" | sed "s/$watchPostfix//g" | sed "s/$backslash//g" `"

	echo "$vidId"
	echo `grep "$vidId" watch.txt`

	echo "[InternetShortcut]" > "$filenameVid"
	echo "URL=https://youtube.com/"`cat watch.txt | sed -n "$(( i )),$(( i ))p"` >> "$filenameVid"

	url=$(echo "https://i."`cat img.txt | grep "$vidId"`)
	echo $url
	curl --output "$filenameImg" $url

done

mv watch.txt watch_old.txt 
mv you_name.txt you_name_old.txt 
mv you_name2.txt you_name2_old.txt 
mv img.txt img_old.txt
rm "$youtube_file"

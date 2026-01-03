#!/bin/bash
cd "$(dirname "$0")"
if [ -z $1 ];
then
	echo "enter filename"
	exit
fi

ls | egrep .*.url | awk '{system("rm " "\""$0"\"")}'

youtube_file=$1
echo $youtube_file

cat $youtube_file | sed 's/\"/\n/g' | sed 's/:/ /g' | egrep -o "(watch\?.*)" > watch.txt
cat $youtube_file | sed 's/"/\n/g' | egrep -o "(https://i.ytimg.com/vi_webp.*)" > img.txt
cat $youtube_file | sed 's/\"/\n/g' | sed 's/:/ /g' | egrep -A6 "title" | egrep -A2 text$ | sed 's/\\u0026//g' | egrep -v "text|--|^ " > you_name.txt

watchPrefix="watch?v="
watchPostfix="\u0026.*"
backslash="\\\\"
bottomStripCount=9
watchLen=`cat watch.txt | wc -l`
len=`cat you_name.txt | wc -l`
topStripCount=$(( $len - $watchLen - $bottomStripCount ))

cat you_name.txt | sed -n "$(( topStripCount + 1)),$(( len - bottomSripCount ))p" > you_name2.txt

for i in $(seq 1 $watchLen) 
do
	
	filename_tmp=$(cat you_name2.txt | sed -n "$(( i )),$(( i ))p" | sed 's/[^a-zA-Z0-9\ ]/-/g' )
	filenameImg="images/"$filename_tmp".png"
	filenameVid=$filename_tmp".url"
	vidId="`cat watch.txt | sed -n "$(( i )),$(( i ))p" | sed "s/$watchPrefix//g" | sed "s/$watchPostfix//g" | sed "s/$backslash//g" `"

	echo "$vidId"
	echo `grep "$vidId" watch.txt`

	echo "[InternetShortcut]" > "$filenameVid"
	echo "URL=https://youtube.com/"`cat watch.txt | sed -n "$(( i )),$(( i ))p"` >> "$filenameVid"

	url=$(echo "`cat img.txt | grep "$vidId" | sed 's/\n//g'`")
	echo $url
	curl --output "$filenameImg" $url

done

mv watch.txt watch_old.txt 
mv you_name.txt you_name_old.txt 
mv you_name2.txt you_name2_old.txt 
mv img.txt img_old.txt
rm "$youtube_file"

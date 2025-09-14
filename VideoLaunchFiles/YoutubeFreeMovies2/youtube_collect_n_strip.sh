#!/bin/bash
cd "$(dirname "$0")"
autoHotKey=$1
autoHotKey_file=$2
scrape_filename=$3

"$autoHotKey" "$autoHotKey_file"

./youtube_strip.sh $scrape_filename

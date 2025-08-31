#!/bin/bash
egrep -o "tubitv.com/movies/[0-9]*/[^\".]*" tubiCollection.txt | awk '{system("./toUrl.sh " $NF)}'

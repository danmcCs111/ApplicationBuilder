#!/bin/bash
fileName="tubiHomePage.txt"
egrep -o "href=\"/category/[/a-z_^\"]+" $fileName | sed 's/href=//g' | awk '{system(" echo tubitv.com" $NF)}'

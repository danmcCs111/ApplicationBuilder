#!/bin/bash
find | grep .java | awk '{system("wc -l " $NF)}' | sort -n 

#!/bin/bash
movies=`ls images/*.png`

for mov in ${movies[@]};
do
	movC=`echo $mov | sed 's/.url//g'`
	mv $mov $movC
done

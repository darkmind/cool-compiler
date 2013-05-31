#!/bin/bash

make clean
make

NUM=0
for file in /usr/class/cs143/archive/cs143.1056/tests/PA5/*
do
	NUM=$(($NUM+1))
	if [[ "$file" == *.cool ]];
	then
		./mycoolc -t $file
		FILENAME=$file".s"
		REFFILEOUT=$file".out"
		echo "spimming "$FILENAME
		/usr/class/cs143/bin/spim -file $FILENAME > a
		diff a $REFFILEOUT >> testresults
		rm -f a
		echo $NUM" -------------------------------------------------------"
	fi
done

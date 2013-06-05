#!/bin/bash

make clean
make

NUM=0
for file in ref_tests/*
do
	NUM=$(($NUM+1))
	if [[ "$file" == *.cl ]];
	then
		./mysemant $file > z
		PREFIX=${file%.cl}
		echo $PREFIX
		REFFILE=$PREFIX".semant"
		echo "#############################"$REFFILE
		diff $REFFILE z
		rm -f z
		echo $NUM" -------------------------------------------------------"
	fi
done

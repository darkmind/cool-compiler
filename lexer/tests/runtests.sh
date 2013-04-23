#!/bin/bash

cd ..
make clean
make lexer
cd tests

cd stresstests
python create_stress_tests.py
cd ../..

for file in tests/stresstests/*
do
	if [[ "$file" == *.cl ]];
	then
		./mycoolc $file 2> a
		/usr/class/cs143/bin/coolc $file 2> b
		diff a b
		rm -f a
		rm -f b
	fi
done

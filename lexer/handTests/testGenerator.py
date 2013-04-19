#! /usr/bin/python

import os

test_dir = ""
ar = [1, 2, 3, 4, 5, 10, 1001, 100000001]
with open(test_dir + 'intTest.cl', 'w') as f:
  for num in ar:
      num = str(num)
      f.write(num + "\n")
      f.write("\v" + num + "\v" + "\n")
      f.write("\f" + num + "\f" + "\n")
      f.write("\t" + num + "\t" + "\n")

keywords = ['class', 'else', 'false', 'fi', 'if', 'in', 'inherits', 'isvoid', 'let', 'loop', 'pool', 'then', 'while', 'case', 'esac', 'new', 'of', 'not', 'true']
with open(test_dir + 'keyTest.cl', 'w') as f:
  for key in keywords:
      f.write(key + "\n")
      f.write("\v" + key + "\v" + "\n")
      f.write("\f" + key + "\f" + "\n")
      f.write("\t" + key + "\t" + "\n")


with open(test_dir + 'charComboTest.cl', 'w') as f:
  for first in range(0, 128):
    for second in range(0, 128):
	f.write(chr(first) + chr(second) + "\n")
	f.write("\v" + chr(first) + chr(second) + "\v" + "\n")
	f.write("\f" + chr(first) + chr(second) + "\f" + "\n")
	f.write("\t" + chr(first) + chr(second) + "\t" + "\n")

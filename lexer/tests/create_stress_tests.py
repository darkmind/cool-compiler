from random import randint, choice

chars = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "~", "\\0", "\\n", "\\b", "\\t", "\\f", "\\0", "\\n", "\\b", "\\t", "\\f", "\\0", "\\n", "\\b", "\\t", "\\f", "\\0", "\\n", "\\b", "\\t", "\\f", "(", ")", "{", "}", "[", "]", "*", "/", "+", "-", "&", "^", "%", "$", "#", "@", "!", "~", "`", "_", "=", "\\", ":", ";", "'", "\"", "<", ",", ">", ".", "?", "/", " ", " ", " ", " ", " ", " ", "(*", "*)", "(*", "*)", "(*", "*)", "(*", "*)", "(*", "*)", "(*", "*)", "--", "--", "--", "--", "--", "--", "class", "else", "false", "fi", "if", "in", "inherits", "isvoid", "let", "loop", "pool", "then", "while", "case", "esac", "new", "of", "not", "true"]

file_no = 1

for j in range(1000):
	file_name = "files/stresstest" + str(file_no) + ".cl"
	f = open(file_name, 'w')
	string_len = randint(0, 2048)

	string = ""
	for i in range(string_len):
		string += choice(chars)
	
	f.write(string)

	file_no += 1
	f.close()

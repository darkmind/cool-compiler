from random import randint, choice

# testing for string literals
chars = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "~", "\\0", "\\n", "\\b", "\\t", "\\f", "(", ")", "{", "}", "[", "]", "*", "/", "+", "-", "&", "^", "%", "$", "#", "@", "!", "~", "`", "_", "=", "\\", ":", ";", "'", "\"", "<", ",", ">", ".", "?", "/"]

base_start = "class StressTestStrings inherits IO {\n"
base_end = "};"
file_no = 1

for j in range(100):
	file_name = "files/stressteststrings" + str(file_no) + ".cl"
	f = open(file_name, 'w')
	f.write(base_start)
	string_len = randint(0, 1040) # max legal string length is 1024, but go beyond that just to see if the compiler reports an error

	string = ""
	for i in range(string_len):
		string += choice(chars)

	f.write("mystring : String <- \"" + string + "\";\n")
	f.write(base_end)
	file_no += 1
	f.close()

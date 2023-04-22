CUT

Selecting a substring from each line of a text file:

● The -o outputFile flag specifies the name of the output file (in this case, outputFile).
● The -c flag : characters of the input file.
● The -w flag : words (sequences of characters without spaces) in the input file.
● The range parameter specifies the output range and has one of the following forms (here N and K are integers):
○ -K range from start of string to K
○ N - range from N to the end of the line
○ N-K range from N to K

The program processes the input data line by line and for each line returns a part of this line according to the specified range. 
If any of the specified files does not exist, or the -c and -w options are incorrectly specified (exactly one of them must be specified), an error should be thrown. 
If there are not enough characters or words in the string, this is not an error; 
  => in this case, it returns that part of the input that falls within the range.
  
In addition to the program itself, there are automatic tests for it.

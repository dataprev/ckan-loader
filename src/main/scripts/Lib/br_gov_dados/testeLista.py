import shlex
my_splitter = shlex.shlex('''foo, bar, "one, two", three four''', posix=True)
my_splitter.whitespace += ','
my_splitter.whitespace_split = True
print list(my_splitter)

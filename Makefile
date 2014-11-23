SRC=src/*.java
JC=javac
NAME=MainApp
all: 
	$(JC) $(SRC)
	mkdir -p bin/
	mv src/*.class bin/
clean:
	rm -rf bin/

run:
	java -classpath bin/ $(NAME) 500 5 5 9000 9001
doc:
	javadoc -d docs/ src/*.java

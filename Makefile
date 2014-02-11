all:
	javac Tester.java
	javadoc -d docs uk.ac.bradford.pisoc

clean:
	rm -fv Tester.class uk/ac/bradford/pisoc/*.class
	rm -rfv docs/

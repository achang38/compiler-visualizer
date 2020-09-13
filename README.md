# compiler-visualizer
An application that shows what happens when a program is compiled. I created this project because I found what I learned in my Compilers class at UW-Madison very informative and wanted to build on the work I did and create a learning tool to teach others.

# Setup
Installation of Java is required to run this application. Add java to your PATH system variable, then on your shell navigate to the source folder in this repository.
<br />
<br />
If you are on a Linux system, run
```
javac -g -cp ./deps:. *.java
```
followed by
```
java -cp ./deps:. CompilerVisualizer
```
<br />
<br />
Otherwise if you are using Windows replace the colon with a semicolon:
```
javac -g -cp ./deps;. *.java
```
followed by
```
java -cp ./deps;. CompilerVisualizer
```


# Credits
This application was built with help from [Apache Netbeans](https://netbeans.apache.org/) for the interface design and uses [TreeLayout by abego](http://treelayout.sourceforge.net/) for the tree algorithm.

# Author
Allen Chang

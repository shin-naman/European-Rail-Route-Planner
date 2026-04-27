compileServer:
	javac Backend.java BackendInterface.java Frontend.java FrontendInterface.java WebApp.java DijkstraGraph.java BaseGraph.java HashTableMap.java MapADT.java GraphADT.java

startServer: compileServer
	java WebApp

runAllTests:
	javac -cp .:../junit5.jar *.java
	java -jar ../junit5.jar -cp . --scan-class-path

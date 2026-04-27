compileServer:
	javac Backend.java BackendInterface.java Frontend.java FrontendInterface.java WebApp.java DijkstraGraph.java BaseGraph.java HashTableMap.java MapADT.java GraphADT.java

startServer: compileServer
	java WebApp

runAllTests:
	javac -cp .:../junit5.jar BackendTests.java Backend.java BackendInterface.java Frontend.java FrontendInterface.java DijkstraGraph.java BaseGraph.java HashTableMap.java MapADT.java GraphADT.java Graph_Placeholder.java
	java -jar ../junit5.jar -cp . --select-class BackendTests

clean:
	rm -f *.class

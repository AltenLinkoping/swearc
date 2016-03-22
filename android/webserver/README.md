
# Log Web Server

Distribute program logs with websockets over the net. The solution is currently in a prof-of-concept-state.


# How to test

Ther is a prof of concept server with dummy generator for logging.

1. Start dummy log server 

    $ ./runDummyWebServer.sh 

2. Open webpage called logClient.html in browser and see the log messages comming in. 

# Work with

There is a gradle task 'eclipse' to generate a Eclipse project for this project. It uses JUnit and Mockito for testing. It has NanoHTTPD and Java-WebSocket as production code dependencies.  


# How to include into other projects

Explanation to be continued ...



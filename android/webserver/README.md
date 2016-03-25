
# Log Web Server

Distribute program logs with websockets over the net. The solution is currently in a prof-of-concept-state.


# Work with

There is a gradle task 'eclipse' to generate a Eclipse project for this project. It uses JUnit and Mockito for testing. It has NanoHTTPD and Java-WebSocket as production code dependencies.  

1. Create eclipse project.

    $ ../../gradlew eclipse

2. In eclipse - import the project as an "existing eclipse project".


# How to include into other projects

See hot it is done in the [Webserver demo](../webserver-demo/).



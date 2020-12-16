# ThreadPool
Thread pool in Java as a simulation of a house automation.

The exercise is a multi-threaded web server with thread-pooling implemented in Java.
Bonus: Added proper HTTP/1.1 keep-alive behavior to your implementation based on the http-client's capabilities exposed through its request headers.
Additionally, a JavaScript application simulating house automation: pressing a button on a control panel would visually turn on a light, change the temperature or
close the curtains.


* The application does use jQuery

* The components interacts with static server resources (e.g. the heating component retrieves the current temperature from the server and also
sends the desired one back to the server)

## Install
Run in the root of the project folder
```
$ java -cp bin/ server.Server
```
The web server will start in the port 8000 at your localhost working either for Chrome and Firefox


## References
* [Java thread pool executor](https://howtodoinjava.com/java/multi-threading/java-thread-pool-executor-example/)

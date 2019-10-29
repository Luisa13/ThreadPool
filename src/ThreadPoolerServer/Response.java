package ThreadPoolerServer;

import java.io.IOException;
import java.util.HashMap;

public abstract class Response {
	
   HashMap<Integer, String> responseSatus;
	
	public Response() {
		responseSatus = new HashMap<Integer, String>();
		responseSatus.put(200, "OK");
		responseSatus.put(404, "Not Found");
		responseSatus.put(400, "Bad Request");
		responseSatus.put(500, "Internal server error");
	}
	
	public abstract byte[] get() throws IOException;
}

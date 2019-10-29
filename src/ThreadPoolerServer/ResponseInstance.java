package ThreadPoolerServer;



public class ResponseInstance {
	
	public static final String GET 		= "GET";
	public static final String POST 	= "POST";
	public static final String PUT 		= "PUT";
	public static final String DELETE 	= "DELETE";
	public static final String HTTP 	= "HTTP";
	
	
	public static Response getInstance(Request request) {
		Response response = null;
		switch(request.getMethod()) {
			case GET: 	 return new GET(request);
			case POST:	 return new POST(request);
			case PUT: 	 return new PUT(request);
			case DELETE: return new DELETE(request);
			case HTTP: 	 return new HTTP(request);
		}
		
		return response;
	}
}

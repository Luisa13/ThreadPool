package ThreadPoolerServer;

import java.util.ArrayList;
import java.util.List;

public class Request {
	
	private String 			method;
	private List<String> 	methods;
	private String 			path;
	private Boolean 		keepAlive;
	private int 			contentSize;
	
	public Request() {
		this.methods = new ArrayList<String>();
		this.methods.add("GET");
		this.methods.add("POS");
		this.methods.add("PUT");
		this.methods.add("DELETE");
		this.methods.add("HTTP");
		
	}
	
	public void read() {
		
	}
	
	public String getMethod() {
		return this.method;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public Boolean isKeepAlive() {
		return this.keepAlive;
	}
	
	public int getContentSize(){
		return this.contentSize;
	}
}

package ThreadPoolerServer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


public class GET extends Response {
	
	private static final int OK 		  = 200;
	private static final int NOTFOUND 	  = 404;
	private static final int BADREQUEST   = 400;
	private static final int SERVERERROR  = 500;
	private static final String PATHROOT  = "web";
	
	private Request request;
	private int 	responseLength;
	private String 	mimeType;
	private byte[] 	content ;
	
	public GET(Request request) {
		super();
		this.request 		= request;
		this.responseLength = 0;
		this.mimeType 		= "text/html; charset=iso-8859-1";
	}
	
	@Override
	public byte[] get(){
		byte[] response 		= null;
		int statusCode 			= OK;
		String pathFromRequest 	= request.getPath();
		
		if (!pathFromRequest.isEmpty() || pathFromRequest != null) {
			Path path = Paths.get(PATHROOT + File.separator + pathFromRequest);
			System.out.println(path);
			if(!Files.exists(path))
				statusCode = NOTFOUND;
			else {
				try {
					this.content   = Files.readAllBytes(path);
					this.mimeType  = Files.probeContentType(path);
					this.responseLength = content.length;
				} catch (IOException e) {
					statusCode = SERVERERROR;
				}
			}
		}else
			statusCode = SERVERERROR;
		
		response = this.getResponse(statusCode);

		return response;
	}
	
	/**
	 * Get the whole response based on the status code
	 * 
	 * */
	private byte[] getResponse(int statusCode){
		byte[] response = null;
		Date date 		= new Date();

		this.responseLength = this.content.length;
		String statusValue = this.responseSatus.getOrDefault(statusCode, "Internal server error").toString();
		
		String header = "HTTP/1.1 " + statusCode + " " + statusValue + "\r\n" + "Date: " + date.toString() + "\r\n"
				+ "Server: Server" + "\r\n" + "Content-Length: " + this.responseLength + "\r\n"
				+ "Connection: Closed \r\n" + "Content-Type: " + this.mimeType + "\r\n" + "\r\n";
		System.out.println("mimeType: " + header);
		String fileText = header;
		response = fileText.getBytes(StandardCharsets.ISO_8859_1);

		if (responseLength > 0) {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

			try {
				byteStream.write(response);
				byteStream.write(content);
				response = byteStream.toByteArray();
			} catch (IOException e) {
				System.err.println("Error while getting html content. " + e.getMessage());
			}
		}
		
		return response;
	}

}

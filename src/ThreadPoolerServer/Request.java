package ThreadPoolerServer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Request {

	private String 			method;
	private List<String> 	methods;
	private String 			path;
	private String 			boundary;
	private byte[] 			byteContent;
	private Boolean 		keepAlive;
	private int 			contentSize;

	public Request() {
		this.methods = new ArrayList<String>();
		this.methods.add("GET");
		this.methods.add("POS");
		this.methods.add("PUT");
		this.methods.add("DELETE");
		this.methods.add("HTTP");
		this.byteContent = null;
		this.boundary = null;

	}

	/**
	 * Read the request from the server
	 * 
	 * @param input
	 * */
	public void read(InputStream input) throws IOException {
		
		this.processHeader(input);

		// Read content (if any)
		int localSize = this.contentSize;
		int chunk = 5000;
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		boolean firstBoundary = true;
		while (localSize > 0) {
			byte[] buffer = new byte[chunk];
			int result = input.read(buffer, 0, chunk);

			// Be careful about boundary if multi-part data
			if (boundary != null) {
				String strContent = new String(buffer, StandardCharsets.ISO_8859_1);
				int boundaryIndex = strContent.indexOf(boundary);
				if (boundaryIndex >= 0 && firstBoundary) {
					int newlineIndex = strContent.indexOf("\r\n\r\n", boundaryIndex);
					int offset = newlineIndex + 4;
					byteStream.write(buffer, offset, (result - offset));
					firstBoundary = false;
				} else if (boundaryIndex >= 0 && !firstBoundary) {
					byteStream.write(buffer, 0, boundaryIndex);
				} else {
					byteStream.write(buffer, 0, result);
				}
			} else {
				byteStream.write(buffer, 0, result);
			}
			localSize -= result;
		}
		this.byteContent = byteStream.toByteArray();
	}
	
	/**
	 * Process the header from a BufferedInputStream
	 * 
	 * @param input
	 * */
	private void processHeader(InputStream input)throws IOException {
		String header = readHeader(input);
		String[] lines = header.split("\r\n");
		for (int i=0; i<lines.length; i++) {
			parseHeaderLine(lines[i]);
		}
	}
	
	private String readHeader(InputStream input) throws IOException {
		String s = "";
	    int i;
	    while((i = input.read()) != -1) {
	        char c = (char) i;
	        s += c;
	       
	        if (s.endsWith("\r\n\r\n")) {
	        	break;
	        }
	    }
	    return s.trim();
	}
	
	public void parseHeaderLine(String line) {
		// Method line
		// Extract method and path from it
		for (String method : methods) {
			if (line.startsWith(method)) {
				this.method = method;
				StringTokenizer tokenizer = new StringTokenizer(line, " ");
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken().trim();
					if (token.equals(method)) {
						String path = tokenizer.nextToken().trim();
						this.path = path;
						if (path.equals("/")) {
							this.path = "index.html";
						}
					}
				}
				break;
			}
		}
		if (line.startsWith("Connection") && line.contains("keep-alive")) {
			this.keepAlive = true;
		}
		if (line.startsWith("Content-Length")) {
			StringTokenizer tokenizer = new StringTokenizer(line, ":");
			tokenizer.nextToken();
			String contentLengthStr = tokenizer.nextToken().trim();
			int contentLength = Integer.parseInt(contentLengthStr);
			this.contentSize = contentLength;
		}
		if (line.contains("boundary")) {
			String boundaryToken = line.substring(line.indexOf("boundary="));
			boundary = "--" + boundaryToken.replace("boundary=", "");
		}
		
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

	public int getContentSize() {
		return this.contentSize;
	}

	public byte[] getByteContent() {
		return this.byteContent;
	}

	public String getBoundary() {
		return this.boundary;
	}
}

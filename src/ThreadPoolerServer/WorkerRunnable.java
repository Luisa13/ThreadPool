package ThreadPoolerServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class WorkerRunnable implements Runnable{

	protected Socket clientSocket = null;
	protected byte[] serverResponse = null;
	int responseLength = 0;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();
            
            //########################## GET HTML ##################################
            Date date = new Date();
            String mimeType = "text/html; charset=iso-8859-1";
            Path path = Paths.get("web/index.html");
            byte[] content = Files.readAllBytes(path);
            responseLength = content.length;
            
            String header = "HTTP/1.1 " + 200 + " " + "OK" + "\r\n" +
					"Date: " + date.toString() + "\r\n" +
					"Server: Server" + "\r\n" + 
					"Content-Length: " + responseLength + "\r\n" +
					"Connection: Closed \r\n" +
					"Content-Type: " + mimeType + "\r\n" +
					"\r\n";
            
            String fileText = header;
            this.serverResponse = fileText.getBytes(StandardCharsets.ISO_8859_1);
            
            if (responseLength > 0) {
    			ByteArrayOutputStream byteStream = new ByteArrayOutputStream( );

    			try {
    				byteStream.write(this.serverResponse);
    				byteStream.write(content);
    				this.serverResponse = byteStream.toByteArray();
    			} catch (IOException e) {
    				System.err.println("Error while getting html content. " + e.getMessage());
    			}
    		}
            
            
          //############################################################
            output.write(this.serverResponse);
            
			/*
			 * output.write(( "HTTP/1.1 200 OK\n\nWorkerRunnable: " + this.serverText +
			 * " - " + time + "" ).getBytes());
			 */
            output.close();
            input.close();
            System.out.println("Request processed: " + time);	
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
 
}

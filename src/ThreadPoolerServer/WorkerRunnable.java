package ThreadPoolerServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

public class WorkerRunnable implements Runnable{

	protected Socket clientSocket = null;
	protected byte[] serverResponse = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            InputStream input  	= clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();

            Request request = new Request();
            request.read(input);
            Response response = ResponseInstance.getInstance(request);
            this.serverResponse = response.get();
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

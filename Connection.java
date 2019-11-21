/**
 * This is the separate thread that services each
 * incoming echo client request.
 *
 * @author Greg Gagne 
 */

import java.net.*;
import java.io.*;

public class Connection implements Runnable
{
	private Socket	client;
	
	public Connection(Socket client) {
		this.client = client;
	}

	public static final int BUFFER_SIZE = 256;

	public void process(Socket client) throws java.io.IOException {
		byte[] buffer = new byte[BUFFER_SIZE];
		InputStream  fromClient = null;
		OutputStream toClient = null;
		
		try {
			/**
			 * get the input and output streams associated with the socket.
			 */
			fromClient = new BufferedInputStream(client.getInputStream());
			toClient = new BufferedOutputStream(client.getOutputStream());
			int numBytes;
			
			/** continually loop until the client closes the connection */
			while ( (numBytes = fromClient.read(buffer)) != -1) {
				toClient.write(buffer,0,numBytes);
				toClient.flush();
			}
   		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
		finally {
			// close streams and socket
			if (fromClient != null)
				fromClient.close();
			if (toClient != null)
				toClient.close();
		}
	}
    /**
     * This method runs in a separate thread.
     */	
	public void run() { 
		try {
		process(client);
		} catch(IOException ioe) {
			System.out.println(ioe);
		}
	}
}


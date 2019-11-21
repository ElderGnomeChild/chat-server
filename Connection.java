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
		BufferedReader fromClient = null;
		OutputStream toClient = null;
		String username = "";
		
		try {
			/**
			 * get the input and output streams associated with the socket.
			 */
			fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			toClient = new BufferedOutputStream(client.getOutputStream());
			int numBytes;
			
			username += fromClient.readLine();

			System.out.println(username);

			/* TODO: USE ARRAYLIST TO KEEP TRACK OF CONNECTIONS
				*  ADD TO ARRAYLIST WHEN SOMEONE JOINS
				*  REMOVE FROM ARRAYLIST WHEN SOMEONE LEAVES
			*/
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


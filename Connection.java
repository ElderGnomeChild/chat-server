/**
 * @author - Daniel Lier and Preston McIllece.
 */

import java.net.*;
import java.io.*;

public class Connection implements Runnable
{
	private Socket client;
	private String username;
	
	public Connection(Socket client) {
		this.client = client;
		this.username = "";
	}

	public Connection(Socket client, String username) {
		this.client = client;
		this.username = username;
	}

	public void process(Socket client) throws java.io.IOException {
		BufferedReader fromClient = null;
		String username = "";
		
		try {
			fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			username += fromClient.readLine();
			this.username = username;

			/* TODO: USE ARRAYLIST TO KEEP TRACK OF CONNECTIONS
				*  ADD TO ARRAYLIST WHEN SOMEONE JOINS
				*  REMOVE FROM ARRAYLIST WHEN SOMEONE LEAVES
			*/

			while(true) {
				String line = fromClient.readLine();
				System.out.println(line);
			}
   		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
		finally {
			// close streams and socket
			if (fromClient != null)
				fromClient.close();
		}
	}
	public void run() { 
		try {
		process(client);
		} catch(IOException ioe) {
			System.out.println(ioe);
		}
	}

	public String toString() {
		String clientString = this.client.toString();
		String returnValue = clientString.concat(this.username);
		return returnValue;
	}

	public void sendMessage(String message) throws IOException {
		BufferedOutputStream toClient = null;
		byte[] buffer = new byte[1024];
		try {
			toClient = new BufferedOutputStream(client.getOutputStream());
			int numBytes;
			toClient.write(message.getBytes());

		} catch (IOException ioe) {
			System.out.println(ioe);
		}

	}
}


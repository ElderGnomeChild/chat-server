/**
 * @author - Daniel Lier and Preston McIllece.
 */

import java.net.*;
import java.io.*;
import java.util.Vector;
import java.util.ArrayList;

public class Connection implements Runnable
{
	private Socket client;
	private String username;
	private Vector messages;
	private ArrayList outputStreams;
	
	public Connection(Socket client, Vector vector, ArrayList arrayList) {
		this.client = client;
		this.username = "";
		this.messages = vector;
		this.outputStreams = arrayList;
	}

	public void process(Socket client) throws java.io.IOException {
		BufferedReader fromClient = null;
		String username = "";
		// BufferedOutputStream toClient = null;
		// PrintWriter toClient = null;
		
		try {
			fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			username += fromClient.readLine();
			System.out.println("raw: " + username);
			String statusCode = this.join(username);
			System.out.println("username: " + this.username);
			System.out.println("status: " + statusCode);
			fromClient.readLine();
			
			// toClient = new BufferedOutputStream(client.getOutputStream());
			// toClient= new PrintWriter(client.getOutputStream());

			outputStreams.add(client.getOutputStream());
			while(true) {
				String line = fromClient.readLine();
				messages.add(line);
				// System.out.println(line);
				// System.out.println(messages);
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


	public String join(String raw) {
		String[] delims = raw.split("\\|");
		// for (String split : delims) {
		// 	System.out.println(split);
		// }
		if (delims[0].equals("JOIN")) {
			if (delims[1].length() <= 15) {
				this.username = delims[1];
				return "STAT|200";
			}
			else {return "STAT|400";}
		}

		else {
			return "STAT|400";
		}

		// return "yuh";
	}
}


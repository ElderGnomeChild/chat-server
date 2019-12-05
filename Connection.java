/**
 * @author - Daniel Lier and Preston McIllece.
 */

import java.net.*;
import java.io.*;
import java.util.Vector;
import java.util.ArrayList;
import java.util.HashMap;

public class Connection implements Runnable
{
	private Socket client;
	private String username;
	private Vector messages;
	private ArrayList outputStreams;
	private HashMap usernameDictionary;
	
	public Connection(Socket client, Vector vector, ArrayList arrayList, HashMap hashMap) {
		this.client = client;
		this.username = "";
		this.messages = vector;
		this.outputStreams = arrayList;
		this.usernameDictionary = hashMap;
	}

	public void process(Socket client) throws java.io.IOException {
		BufferedReader fromClient = null;
		String username = "";
		PrintWriter printWriter = null;
		
		try {
			fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			username += fromClient.readLine();
			System.out.println("raw: " + username);
			String statusCode = this.join(username);
			fromClient.readLine();
			
			printWriter = new PrintWriter(this.client.getOutputStream());
			System.out.println("status: " + statusCode);
			printWriter.println(statusCode);
			printWriter.flush();

			if (!statusCode.equals("STAT|200")) {
				client.close();
			}
			
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
	
	
	public String join(String raw) throws IOException {
		String[] delims = raw.split("\\|");
		try {
			if (delims[0].equals("JOIN")) {
				if (delims[1].length() <= 15) {
					if (this.usernameDictionary.putIfAbsent(delims[1], this.client.getOutputStream()) == null) {
						this.username = delims[1];
						this.outputStreams.add(this.client.getOutputStream());
						return "STAT|200";
					}
					else {return "STAT|420";}
				}
				else {return "STAT|420";}
			}
			else {return "STAT|400";}

		} catch (IOException ioe) {
			System.out.println(ioe);
			return "STAT|400";
		}
	}
}


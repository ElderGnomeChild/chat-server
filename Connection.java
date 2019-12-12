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
	private HashMap<String, OutputStream> usernameDictionary;
	private boolean broadcast = true;
	
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
			String statusCode = this.parse(username, fromClient);
			fromClient.readLine();
			
			printWriter = new PrintWriter(this.client.getOutputStream());
			printWriter.println(statusCode);
			printWriter.flush();
			boolean joinMessage = true;

			if (!statusCode.equals("STAT|200")) {
				client.close();
				joinMessage=false;
			}
		
			while(true) {
				if (joinMessage) {
					messages.add(username);
					joinMessage = false;
				}
				String line = fromClient.readLine();
				String status = "";
				if (line.contains("|")){
					status = parse(line, fromClient);
				}
				
				if (status.length() > 0 && !status.equals("leaving")) {	
					printWriter.println(status);
					printWriter.flush();
				}

				if (this.broadcast) {
					messages.add(line);
				}
				this.broadcast = true;

				if (status.equals("leaving")) {
					if (fromClient != null)
					fromClient.close();
					if (printWriter != null)
					printWriter.close();
					if (client != null)
					client.close();
					break;
				}
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
			System.err.println(ioe);
		}
	}
	
	public String toString() {
		String clientString = this.client.toString();
		String returnValue = clientString.concat(this.username);
		return returnValue;
	}
	
	public String parse(String raw, BufferedReader br) throws IOException {
		String[] delims = raw.split("\\|");
		PrintWriter printer = null;
		try {
			if (delims[0].equals("JOIN")) {
				if (delims[1].length() <= 15) {
					if (this.usernameDictionary.putIfAbsent(delims[1], this.client.getOutputStream()) == null) {
						this.username = delims[1];
						this.outputStreams.add(this.client.getOutputStream());
						return "STAT|200";
					}
					else {
						this.broadcast = false;
						return "STAT|420";}
				}
				else {
					this.broadcast = false;
					return "STAT|420";
				}
			}
			else if (delims[0].equals("PVMG")) {
				String message = br.readLine();
				String toWho = delims[2];
				if (this.usernameDictionary.containsKey(toWho)) {
					printer = new PrintWriter(this.usernameDictionary.get(toWho));
					printer.println(raw);
					printer.flush();
					printer.println(message);
					printer.flush();
					this.broadcast = false;
					return "STAT|200";
				}
				else{return "STAT|421";}
			}
			else if (delims[0].equals("BDMG")) {
				return "STAT|200";
			}
			else if (delims[0].equals("LEAV")) {
				String who = delims[1];
				OutputStream stream = this.usernameDictionary.get(who);
				this.outputStreams.remove(stream);
				this.usernameDictionary.remove(who);
				return "leaving";
			}
			else {return "STAT|666";}

		} catch (IOException ioe) {
			System.err.println(ioe);
			return "STAT|400";
		}
	}

	// public Boolean pm(String header) throws IOException {
	// 	String[] delims = header.split("\\|");
	// 	try {
	// 		if (delims[0].equals("PVMG")){
	// 			String message = fromClient.readLine();
				
	// 		}
	// 	}
	// }catch (IOException ioe) {System.err.println(ioe);}
}


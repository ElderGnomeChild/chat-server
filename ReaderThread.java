/**
 * @author - Daniel Lier and Preston McIllece.
 */

import java.io.*;
import java.net.*;
import javax.swing.*;

public class ReaderThread implements Runnable
{
	Socket server;
	BufferedReader fromServer;

	public ReaderThread(Socket server) {
		this.server = server;
	}

	public void run() {
		try {
			fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));

			while (true) {
				String message = fromServer.readLine();
				message = parse(message);
				System.out.println(message);
			}
		}
		catch (IOException ioe) { System.out.println(ioe); }

	}

	private String parse(String message) {
		if (message.contains("|")) {
			String[] delims = message.split("\\|");
			if (delims[0].equals("JOIN")) {
				return delims[1] + " has joined the chat.";
			}
			else if (delims[0].equals("BDMG")) {
				try {
				String msg = fromServer.readLine();
				return delims[1] + ": " + msg;
				} catch(IOException ioe) { System.out.println(ioe); }
			}
			else if (delims[0].equals("PVMG")){
				try {
					String line = fromServer.readLine();
					String msg = line.substring(line.indexOf(" ") + 1);

					return delims[1] + "(private)🍑 : " + msg;
					} catch(IOException ioe) { System.out.println(ioe); }
			}
			else if (delims[0].equals("STAT")) {
				if (delims[1].equals("421")) {
					return "Sorry lad, that username doesn't exist.";

				}
				else if (!delims[1].equals("200")) {
					return "Oopsy. Your message was lost champ.🤷‍♂️";
				}
				return "👍";
			}
			else if (delims[0].equals("LEAV")) {
				return delims[1] + " has left the chat.👋";
			}
			return message;}
		else {return message;}
	}
}

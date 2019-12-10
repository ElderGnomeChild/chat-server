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
			
			return message;
		}
		else {
			return message;
		}
	}
}

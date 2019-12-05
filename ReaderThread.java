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
				// System.out.println("receiving");
				String message = fromServer.readLine();
				System.out.println(message);
				// System.out.println("got it");
			}
		}
		catch (IOException ioe) { System.out.println(ioe); }

	}
}

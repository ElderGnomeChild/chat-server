/**
 * @author - Daniel Lier and Preston McIllece.
 */

import java.net.*;
import java.util.concurrent.*;
import java.io.*;
import java.util.*;

public class ChatClient
{
	public static final int DEFAULT_PORT = 1337;
	private static final Executor exec = Executors.newCachedThreadPool();
	

	private static int parseStatusCode(String statusCode) {
		return Integer.parseInt(statusCode.substring(statusCode.indexOf("|") + 1));
	}


	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage: java EchoClient <echo server>");
			System.exit(0);
		}
		
		PrintWriter networkPout = null;		// the writer to the network
		BufferedReader localBin = null;		// the reader from the local keyboard
		Socket sock = null;			// the socket
		ReaderThread reader = null;
		BufferedReader listeningForStatusCode = null;
		
		try {
			sock = new Socket(args[0], DEFAULT_PORT);
			localBin = new BufferedReader(new InputStreamReader(System.in));
			networkPout = new PrintWriter(sock.getOutputStream(),true);
			
			/**
			 * Read from the keyboard and send it to the echo server.
			 * Quit reading when the client enters a period "."
			 */
			System.out.println("Enter a username:");
			String name = localBin.readLine();
			String joinString = "JOIN|" + name + "|all|date|\r\n" + name + "\r\n";
			networkPout.println(joinString);
			
			
			listeningForStatusCode = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String statusCode = listeningForStatusCode.readLine();
			int statusCodeNumber = parseStatusCode(statusCode);

			if (statusCodeNumber == 200) {
				System.out.println("Welcome " + name + "!");

				reader = new ReaderThread(sock);
				exec.execute(reader);
				
				boolean done = false;
				while (!done) {
					String line = localBin.readLine();
					if (line.equals("."))
						done = true;
					else {
						networkPout.println(line);
					}
				}
			}
			else if (statusCodeNumber == 420) {
				if (name.length() > 15) {
					System.out.println("\r\nSorry, your username must be 15 characters or fewer! \r\nPlease try again!");
				}
				else {
					System.out.println("\r\nSorry the username \"" + name + "\" is already taken! \r\nPlease try again!");
				}
			}
			else {
				System.out.println("We have no clue what happened. Please contact server owner.");
			}

		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
		finally {
			if (localBin != null)
				localBin.close();
			if (networkPout != null)
				networkPout.close();
			if (sock != null)
				sock.close();
		}
	}
}

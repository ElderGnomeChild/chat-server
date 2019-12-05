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
	
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage: java EchoClient <echo server>");
			System.exit(0);
		}
		
		// BufferedReader networkBin = null;	// the reader from the network
		PrintWriter networkPout = null;		// the writer to the network
		BufferedReader localBin = null;		// the reader from the local keyboard
		Socket sock = null;			// the socket
		ReaderThread reader = null;
		
		try {
			sock = new Socket(args[0], DEFAULT_PORT);
			reader = new ReaderThread(sock);
			exec.execute(reader);
			
			// set up the necessary communication channels
			// networkBin = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			localBin = new BufferedReader(new InputStreamReader(System.in));
			
			/**
			 * a PrintWriter allows us to use println() with ordinary
			 * socket I/O. "true" indicates automatic flushing of the stream.
			 * The stream is flushed with an invocation of println()
			 */
			networkPout = new PrintWriter(sock.getOutputStream(),true);
			
			/**
			 * Read from the keyboard and send it to the echo server.
			 * Quit reading when the client enters a period "."
			 */
			System.out.println("Enter a username:");
			String name = localBin.readLine();
			name = "JOIN|" + name + "|all|date|\r\n" + name + "\r\n";
			networkPout.println(name);



			boolean done = false;
			while (!done) {
				String line = localBin.readLine();
				if (line.equals("."))
					done = true;
				else {
					networkPout.println(line);
				}
			}


			/* The client application must be concerned with both reading data 
			 * from the user and reading data from the socket. Reading data from 
			 * the user is handled by the usual event mechanisms of implementing 
			 * the actionPerformed() method from the ActionListener interface
			*/
		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
		finally {
			// if (networkBin != null)
			// 	networkBin.close();
			if (localBin != null)
				localBin.close();
			if (networkPout != null)
				networkPout.close();
			if (sock != null)
				sock.close();
		}
	}
}

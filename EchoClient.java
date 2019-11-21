/**
 * An echo client. The client enters data to the server, and the
 * server echoes the data back to the client.
 *
 * @author - Greg Gagne
 */


/***
 * Modified by Daniel Lier for Computer Networks HW2
*/

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class EchoClient
{
	public static final int DEFAULT_PORT = 1337;			// changed the port here
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader networkBin = null;	// the reader from the network
		PrintWriter networkPout = null;		// the writer to the network
		BufferedReader localBin = null;		// the reader from the local keyboard
		Socket sock = null;			// the socket
		String message;
		
		try {
			sock = new Socket("localhost", DEFAULT_PORT);
			message = args[0];								//set addr to second arg, this will be sent to the sever to be converted to an IP address
			
			// set up the necessary communication channels
			networkBin = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			localBin = new BufferedReader(new InputStreamReader(System.in));
			
			/**
			 * a PrintWriter allows us to use println() with ordinary
			 * socket I/O. "true" indicates automatic flushing of the stream.
			 * The stream is flushed with an invocation of println()
			 */
			networkPout = new PrintWriter(sock.getOutputStream(),true);
			
			/**
			 * Read from the string and send it to the echo server.
			 */
				String line = message;						
				networkPout.println(line);					//send addr to client
				System.out.println("Server: " + networkBin.readLine()); //print client response

		}	
		catch (IOException ioe) {
			System.err.println(ioe);
		}
		finally {
			if (networkBin != null)
				networkBin.close();
			if (localBin != null)
				localBin.close();
			if (networkPout != null)
				networkPout.close();
			if (sock != null)
				sock.close();
		}
	}
}

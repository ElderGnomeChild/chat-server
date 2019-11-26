/**
 * @author - Daniel Lier and Preston McIllece.
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.*;

public class ChatServer
{
	public static final int DEFAULT_PORT = 1337;

    // construct a thread pool for concurrency	
	private static final Executor exec = Executors.newCachedThreadPool();
	private static ArrayList currentConnections = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
		ServerSocket sock = null;
		
		try {
			// establish the socket
			sock = new ServerSocket(DEFAULT_PORT);
			
			while (true) {
				Runnable task = new Connection(sock.accept());
				currentConnections.add(task);
				
				exec.execute(task);
			}
		}
		catch (IOException ioe) { System.err.println(ioe); }
		finally {
			if (sock != null)
				sock.close();
		}
	}
}

/**
 * @author - Daniel Lier and Preston McIllece.
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.*;


public class ChatServer
{
	public static final int DEFAULT_PORT = 1337;

    // construct a thread pool for concurrency	
	private static final Executor exec = Executors.newCachedThreadPool();
	public static Vector messages = new Vector<String>();
	public static ArrayList outputStreams = new ArrayList<OutputStream>();
	public static BroadcastThread broadcastThread = new BroadcastThread(messages, outputStreams);

	public static void main(String[] args) throws IOException {
		ServerSocket sock = null;
		
		try {
			// establish the socket
			sock = new ServerSocket(DEFAULT_PORT);
			
			exec.execute(broadcastThread);
			while (true) {
				Connection task = new Connection(sock.accept(), messages, outputStreams);
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

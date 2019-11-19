import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class  SimpleServer
{
	public static final int DEFAULT_PORT = 1337;

    // construct a thread pool for concurrency	
	private static final Executor exec = Executors.newCachedThreadPool();
    
    private ServerSocket sprock = null;
    private Runnable fast = null;
    private final Executor exex= Executors.newCachedThreadPool();

    public void start() throws IOException{

            try {
                sprock = new ServerSocket(DEFAULT_PORT);

                while(true) {
                    fast = new Connection(sprock.accept());
                    exex.execute(fast);
                }
            } catch (IOException ioe) { 
                System.err.println(ioe); 
            } finally {
                sprock.close();
            }
    }

	public static void main(String[] args) throws IOException {
		// ServerSocket sock = null;
		
		// try {
		// 	// establish the socket
		// 	sock = new ServerSocket(DEFAULT_PORT);
			
		// 	while (true) {
		// 		/**
		// 		 * now listen for connections
		// 		 * and service the connection in a separate thread.
		// 		 */
		// 		Runnable task = new Connection(sock.accept());
		// 		exec.execute(task);
		// 	}
		// }
		// catch (IOException ioe) { System.err.println(ioe); }
		// finally {
		// 	if (sock != null)
		// 		sock.close();
        // }
        
        SimpleServer server = new SimpleServer();
        server.start();
	}
}
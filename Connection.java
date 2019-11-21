/**
 * This is the separate thread that services each
 * incoming echo client request.
 *
 * @author Greg Gagne 
 */

/**
Modified by Daniel Lier for HW5
*/

import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.net.InetAddress;

import java.text.*;

public class Connection implements Runnable
{
	public static final int BUFFER_SIZE = 1024;
	private Socket	client;
	public static String xml;

	String line;
	String resource;
	
	public Connection(Socket client) {
		this.client = client;
	}

	public void process(Socket client) throws java.io.IOException {
		BufferedReader fromClient = null;
		OutputStream toClient = null;


		try {
			fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			toClient = new BufferedOutputStream(client.getOutputStream());
            
            while(true) {
                String getLine = fromClient.readLine();									//get the first line of the data sent from the client
                System.out.println(getLine);
            }
            // byte[] contents = new byte[BUFFER_SIZE];							//send the file to the client
            // int length;
            // while ((length = fromFile.read(contents)) > 0) {
            //     toClient.write(contents, 0, length);
            //     toClient.flush();
            // }

		} catch (IOException ioe) {
			System.err.println(ioe);
		} finally {																	//close the streams
			// if (fromClient != null) {
			// 	fromClient.close();
			// }
			// if (toClient != null) {
			// 	toClient.close();
			// }
		}

	} 

    /**
     * This method runs in a separate thread.
     */	
	public void run() { 
		try {
			this.process(client);
		}
		catch (java.io.IOException ioe) {
			System.err.println(ioe);
		}
	}
}


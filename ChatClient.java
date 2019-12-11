/**
 * @author - Daniel Lier and Preston McIllece.
 */

import java.net.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.LocalDate;
import java.util.concurrent.*;
import java.io.*;
import java.util.*;

public class ChatClient
{
	public static final int DEFAULT_PORT = 1337;
	private static final Executor exec = Executors.newCachedThreadPool();
	private static BufferedReader localBin = null;		// the reader from the local keyboard
	private static String userName = "";
	

	private static int parseStatusCode(String statusCode) {
		return Integer.parseInt(statusCode.substring(statusCode.indexOf("|") + 1));
	}

	private static String parseDate(Date uglyDate) {
		Instant instant = uglyDate.toInstant();
		LocalDateTime ldt = instant.atOffset(ZoneOffset.UTC).toLocalDateTime();
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyy-MM-dd-HH-mm-ss");
		return ldt.format(timeFormatter);
	}

	private static String broadcast(String name) {
		Date date = new Date();
		return "BDMG|" + name + "|all|" + parseDate(date);
	}
	
	private static String privateMessage(String name, String destination) {
		Date date = new Date();
		return "PVMG|" + name + "|" + destination + "|" + parseDate(date);
	}

	private static String retrieveUsername() throws IOException {
		try {
			System.out.println("Enter a username:");
			return localBin.readLine();
		}
		catch (IOException ioe) {
			System.err.println(ioe);
			return "";
		}
	}


	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage: java EchoClient <echo server>");
			System.exit(0);
		}
		
		PrintWriter networkPout = null;		// the writer to the network
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
			userName = retrieveUsername();
			Date date = new Date();
			boolean stupidUserName = true;

			while (stupidUserName) {
				if (userName.contains("|")) {
					System.out.println("Your username cannot contain '|'. Please try again.\r\n");
					userName = retrieveUsername();
				}
				else {
					stupidUserName = false;
				}
			}
			String joinString = "JOIN|" + userName + "|all|" + parseDate(date) + "\r\n" + userName + "\r\n";
			networkPout.println(joinString);
			
			
			listeningForStatusCode = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String statusCode = listeningForStatusCode.readLine();
			int statusCodeNumber = parseStatusCode(statusCode);
			if (statusCodeNumber == 200) {
				System.out.println("Welcome " + userName + "!");

				reader = new ReaderThread(sock);
				exec.execute(reader);
				
				boolean isLeaving = false;
				while (!isLeaving) {
					String line2 = localBin.readLine();
					String line = "";
					Character at = '@';
					if (at.equals(line2.charAt(0))){
						String destinationUser = line2.substring(1, line2.indexOf(" "));
						line = privateMessage(userName, destinationUser);
					}
					else {line = broadcast(userName);}
					if (line2.equals("."))
						isLeaving = true;
					else {
						networkPout.println(line);
						networkPout.println(line2);
					}
				}
			}
			else if (statusCodeNumber == 420) {
				if (userName.length() > 15) {
					System.out.println("\r\nSorry, your username must be 15 characters or fewer! \r\nPlease try again!");
				}
				else {
					System.out.println("\r\nSorry the username \"" + userName + "\" is already taken! \r\nPlease try again!");
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

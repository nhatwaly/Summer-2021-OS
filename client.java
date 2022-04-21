import java.io.*;
import java.net.*;
import java.util.*;

public class client {
	// driver code
	public static void main(String[] args) {
		// establish a connection by providing host and port number
		try (Socket socket = new Socket("dc02.utdallas.edu", 2612)) {
			// writing to server
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			// reading from server
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// object of scanner class
			Scanner sc = new Scanner(System.in);
			String line = null;
			int clientId;
			// start new thread to print out message from server
			Thread ClientThread = new Thread(new ClientThread(line, sc, out));
			ClientThread.start();
			// getting clientId from server and display it
			line = in.readLine();
			clientId = Integer.parseInt(line);
			System.out.println("Connected to server, this is client number (" + clientId + ")");
			while (!"stop".equalsIgnoreCase(line)) {
				try {
					while (in.ready()) {
						line = in.readLine();
						String lineInput[] = line.split(" ");
						// if "stop" is received
						if (lineInput[0].equals("stop")) {
							if (lineInput.length == 1) {
								System.out.println("The client (" + clientId + ") is closing...");
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									//catch block
									e.printStackTrace();
								}
								// closing socket and exit process
								System.out.println("The client (" + clientId + ") is closed!");
								socket.close();
								System.exit(0);
							} else {
								// do nothing
							}
							// if "send" is received
						} else if (lineInput[0].equals("send")) {
							if (lineInput[1].equals(String.valueOf(clientId))) {
								// do nothing because the message is sent from this client
							} else if (lineInput[2].equals(String.valueOf(0))) {
								System.out.println("Process (" + lineInput[1] + ") sent to all: " + lineInput[3]); // for
																													// send
																													// all
							} else if (lineInput[2].equals(String.valueOf(clientId))) {
								System.out.println("Process (" + lineInput[1] + ") sent to process (" + lineInput[2]
										+ "): " + lineInput[3]); // for send one
							}
						} else {// for general message
							System.out.println("Server replied: " + line);
						}

					}
				} catch (IOException e) {

					e.printStackTrace();
				}

			}
			// closing the scanner object
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

//ClientThread to handle input from user separately from printing from server
class ClientThread implements Runnable {
	private String line;
	private Scanner sc;
	private PrintWriter out;

	public ClientThread(String line, Scanner sc, PrintWriter out) {
		this.line = line;
		this.sc = sc;
		this.out = out;
	}

	public void run() {
		// constantly getting input from user until getting "stop"
		while (!"stop".equalsIgnoreCase(line)) {
			line = sc.nextLine();
			// outputing command to client
			String lineInput[] = line.split(" ");
			if (lineInput[0].equals("stop")) {
				System.out.println("Attempted \"stop\" command...");
			}
			// for "send" command
			else if (lineInput[0].equals("send")) {
				if (lineInput[1].equals(String.valueOf(0))) {
					System.out.println("Attempted \"send\" command to all...");
				} else
					System.out.println("Attempted \"send\" command...");
			}

			// sending the user input to server
			out.println(line);
			out.flush();

		}
	}

}

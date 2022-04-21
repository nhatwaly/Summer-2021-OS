import java.io.*;
import java.net.*;

public class server {
	public static void main(String[] args) {
		ServerSocket server = null;
		int clientId = 0;
		try {
			// server is listening on port 2612
			server = new ServerSocket(2612);
			server.setReuseAddress(true);
			// running infinite loop for getting client request
			while (true) {
				// socket object to receive incoming client requests
				Socket client = server.accept();
				// Displaying that new client is connected to server
				System.out.println("New client connected " + client.getInetAddress().getHostAddress());
				clientId++;
				// displaying clientId of different clients
				System.out.println("This is client number(" + clientId + ")");
				// create a new thread object
				ClientHandler clientSock = new ClientHandler(client, clientId);
				// Thread to handle each client separately
				new Thread(clientSock).start();

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ClientHandler class
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private final int clientId;
		// message String and array for checking if message is received
		public static String message;
		public static int[] messageAvailable = { 0, 0, 0, 0 };

		public ClientHandler(Socket socket, int id) {
			this.clientSocket = socket;
			this.clientId = id;
		}

		// method to write message to clients
		public void writeToClient(String mess) {
			message = mess;
			for (int i = 0; i <= 3; i++) {
				messageAvailable[i] = 1;
			}
		}

		public void run() {
			PrintWriter out = null;
			BufferedReader in = null;
			try {
				// get the outputstream of client
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				// get the inputstream of client
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String line;
				out.println(clientId); // send clientId to client

				while (true) {
					// Constantly sending message to each client whenever message is available
					if (messageAvailable[clientId - 1] == 1) {
						out.println(message);
						messageAvailable[clientId - 1] = 0;
					}
					// Whenever there is a new message from clients
					while (in.ready()) {
						line = in.readLine();
						// writing the received message from client
						System.out.printf("-Sent from the client (" + clientId + "): %s\n", line);
						out.println(line);
						// Split the String into array to manipulate
						String lineInput[] = line.split(" ");
						// for "stop" command
						if (lineInput[0].equals("stop")) {
							if (lineInput.length != 1) {
								out.println("Wrong command! Please type \"stop\" to stop all processes!");
							} else {
								writeToClient("stop");
								System.out.println("Stop all processes now!");
								// Wait for all clients to close first
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// catch block
									e.printStackTrace();
								}
								// And then server close itself
								System.exit(0);
							}
						}
						// for "send" command
						else if (lineInput[0].equals("send")) {
							// check for syntax
							if (lineInput.length == 1 || lineInput.length == 2) {
								out.println("Wrong command! Please follow: send <receiver-id> <MESSAGE>");
							} else if (!(lineInput[1].equals(String.valueOf(0))
									|| lineInput[1].equals(String.valueOf(1)) || lineInput[1].equals(String.valueOf(2))
									|| lineInput[1].equals(String.valueOf(3))
									|| lineInput[1].equals(String.valueOf(4)))) {
								out.println("Wrong command!! Please follow: send <receiver-id> <MESSAGE>");
							} else if (lineInput.length >= 4) {
								out.println("Wrong command!!! Please follow: send <receiver-id> <MESSAGE>");
							} else {// write message to clients
								System.out.println("Send message from process (" + clientId + ") to process ("
										+ lineInput[1] + "): " + lineInput[2]);
								writeToClient("send " + clientId + " " + lineInput[1] + " " + lineInput[2]);

							}

						}

					}

				}

			} catch (

			IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
						clientSocket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

package org.eder.learning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.server.SocketSecurityException;
import java.util.Scanner;

public class SocketTestClient {
	public static void main(String[] args) {
		launchServer();

		for(int i=0;i<2;i++)
			connectClient(i);
	}

	private static void connectClient(int id) {
		final int tid = id;
		new Thread(new Runnable() {
			@Override
			public void run() {
				try (Socket socket = new Socket("127.0.0.1", 8050)) {
					// Version using PrintWriter
					/* try(PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
							Scanner input = new Scanner(System.in)){
						pw.println("Client [" + tid + "]: Hello World");
						String msg;
						System.out.println("Waiting Client "+tid);
						while((msg= input.nextLine())!=null){
							pw.println("Client [" + tid + "]: "+msg);
							System.out.println("Waiting Client "+tid);
						}
					}*/
					try(BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
							Scanner input = new Scanner(System.in)){
						pw.write("Client [" + tid + "]: Hello World");
						pw.newLine();
						pw.flush();
						String msg;
						System.out.println("Waiting Client "+tid);
						while((msg= input.nextLine())!=null){
							pw.write("Client [" + tid + "]: "+msg);
							pw.newLine();
							pw.flush();
							System.out.println("Waiting Client "+tid);
						}
					}

				} catch (UnknownHostException uhe) {
					System.err.println("unknown host: " + uhe.getMessage());
				} catch (IOException ioe) {
					System.err.println("I/O error: " + ioe.getMessage());
					ioe.printStackTrace();
				}
			}
		}).start();
	}

	private static void launchServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SocketTestServer.startServer();
			}
		}).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class SocketTestServer {
	static void startServer() {
		try (ServerSocket server = new ServerSocket(8050, 5)) {
			System.out.println("Server started.");
			while (true) {
				Socket connection = server.accept();
				listening(connection);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void listening(Socket sconnection) {
		final Socket connection = sconnection;
		new Thread(new Runnable() {	
			@Override
			public void run() {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
					String inCommingMessage;
					while ((inCommingMessage = reader.readLine()) != null) {
						System.out.println(inCommingMessage);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
package org.eder.learning;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpTestClient {

	public static void main(String args[]) {
		launchServer();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		connectClient();
	}

	private static void connectClient() {
		try (DatagramSocket connection = new DatagramSocket(); Scanner input = new Scanner(System.in)) {
			byte[] data;
			while(true) {
				System.out.print("> ");
				data = input.nextLine().getBytes();
				DatagramPacket msg = new DatagramPacket(data, data.length, InetAddress.getByName("localhost"), 8051);
				connection.send(msg);
				
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void launchServer() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				UdpTestServer.startServer();
			}
		}).start();
	}
}

class UdpTestServer {

	public static void startServer() {
		try (DatagramSocket connection = new DatagramSocket(8051)) {
			System.out.println("Server started.");
			byte[] receivedData = new byte[10];
			DatagramPacket msg = new DatagramPacket(receivedData, receivedData.length);
			while (true) {
				//byte buffer is not flushed, handle this
				connection.receive(msg);
				System.out.println("Client sent: " + new String(receivedData, 0, msg.getLength()));
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

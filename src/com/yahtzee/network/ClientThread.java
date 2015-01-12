package com.yahtzee.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread {
	
	private Socket socket = null;
	private AppClient client = null;
	private BufferedReader streamIn = null;
	private boolean done = false;

	public ClientThread(AppClient client, Socket socket) {
		this.client = client;
		this.socket = socket;
		this.open();
		this.start();
	}
	
	public void open() {
		try {
			streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch(IOException e) {
			System.out.println("Error getting input stream");
			client.stop();
		}
	}
	
	public void close() {
		done = true;
		try {
			if(streamIn != null)
				streamIn.close();
		} catch(IOException e) {
			System.out.println("Error closing input stream");
		}
	}
	
	public void run() {
		System.out.println("Client Thread " + socket.getLocalPort() + " running");
		while(!done) {
			try {
				client.handle(streamIn.readLine());
			} catch(IOException e) {
				System.out.println("Listening error");
			}
		}
	}

}

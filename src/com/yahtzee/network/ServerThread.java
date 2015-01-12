package com.yahtzee.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	
	private int ID = -1;
	private Socket socket = null;
	private AppServer server = null;
	private BufferedReader streamIn = null;
	private BufferedWriter streamOut = null;
	private boolean done = false;

	public ServerThread(AppServer server, Socket socket) {
		super();
		this.server = server;
		this.socket = socket;
		this.ID = socket.getPort();
	}
	
	public int getID() {
		return this.ID;
	}
	
	public void send(String msg) {
		try {
			streamOut.write(msg);
			streamOut.flush();
		} catch(IOException e) {
			System.out.println(ID + " Error sending message");
//			Trace.exception(e);
			server.remove(ID);
		}
	}
	
	public void run() {
		System.out.println("Server Thread " + ID + " running.");
		while(!done) {
			try {
				server.handle(ID, streamIn.readLine());
			} catch(IOException e) {
				System.out.println(ID + ":Error reading input");
				server.remove(ID);
				break;
			}
		}
	}
	
	public void open() throws IOException {
		System.out.println(ID + ": Opening buffer streams");
		streamIn  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		streamOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}
	
	public void close() throws IOException {
		this.done = true;
		if(socket != null)
			socket.close();
		if(streamIn != null)
			streamIn.close();
	}

}

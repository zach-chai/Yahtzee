package com.yahtzee.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	
	private int ID = -1;
	private Socket socket = null;
	private AppServer server = null;
	private ObjectInputStream streamIn = null;
	private ObjectOutputStream streamOut = null;
	private boolean done = false;
	private boolean isReady = false;

	public ServerThread(AppServer server, Socket socket) {
		super();
		this.server = server;
		this.socket = socket;
		this.ID = socket.getPort();
	}
	
	public int getID() {
		return this.ID;
	}
	
	public void send(Object o) {
		try {
			streamOut.writeObject(o);
			streamOut.flush();
			streamOut.reset();
		} catch(IOException e) {
			System.out.println(ID + " Error sending message");
//			Trace.exception(e);
			server.remove(ID);
		}
	}
	
	public void startRound() {
		send("start round");
	}
	
	public void endRound() {
		send("end round");
	}
	
	public void run() {
		System.out.println("Server Thread " + ID + " running.");
		while(!done) {
			try {
				server.handle(ID, streamIn.readObject());
			} catch(ClassNotFoundException e) {
				System.out.println(ID + ":Error class not found");
				server.remove(ID);			
				break;
			} catch(IOException e) {
				System.out.println(ID + ":Error reading input");
				server.remove(ID);
				break;
			}
		}
	}
	
	public void open() throws IOException {
		System.out.println(ID + ": Opening Object streams");
		streamOut = new ObjectOutputStream(socket.getOutputStream());
		streamOut.flush();
		streamIn  = new ObjectInputStream(socket.getInputStream());
		System.out.println(ID + ": Opened Object Streams");
	}
	
	public void close() throws IOException {
		this.done = true;
		if(socket != null)
			socket.close();
		if(streamIn != null)
			streamIn.close();
	}
	
	public boolean isReady() {
		return isReady;
	}
	
	public void ready() {
		isReady = true;
	}

}

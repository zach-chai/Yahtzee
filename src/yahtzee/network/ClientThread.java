package com.yahtzee.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.yahtzee.model.GameScore;
import com.yahtzee.views.GUIClient;

public class ClientThread extends Thread {
	
	private Socket socket = null;
	private GUIClient client = null;
	private ObjectInputStream streamIn = null;
	private boolean done = false;

//	public ClientThread(AppClient client, Socket socket) {
//		this.client = client;
//		this.socket = socket;
//		this.open();
//		this.start();
//	}
	
	public ClientThread(GUIClient client, Socket socket) {
		this.client = client;
		this.socket = socket;
		this.open();
		this.start();
	}
	
	public void open() {
		System.out.println("opening stream...");
		try {
			streamIn  = new ObjectInputStream(socket.getInputStream());
			System.out.println("stream opened");
		} catch(IOException e) {
			System.out.println("Error getting input stream");
			client.disconnect();
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
				Object o = streamIn.readObject();
				if(o instanceof GameScore) {
					client.updateScoreBoard(((GameScore) o));
				} else {
					String msg = (String) o;
					if("start round".equals(msg)) {
						client.newRound();
					} else if("end round".equals(msg)) {
						client.endRound();
					} else if("game finished".equals(msg)) {
						client.endGame();
					} else {
						client.displayMsg(msg);
					}					
				}
			} catch(IOException e) {
				System.out.println("Listening error");
			} catch (ClassNotFoundException e) {
				System.out.println("class not found error");
			}
		}
	}

}

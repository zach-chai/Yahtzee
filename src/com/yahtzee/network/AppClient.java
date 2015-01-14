package com.yahtzee.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.yahtzee.utils.Config;

public class AppClient implements Runnable{
	
	private int ID = 0;
	private Socket socket = null;
	private Thread thread = null;
	private ClientThread client = null;
	private BufferedReader console = null;
	private BufferedReader streamIn = null;
	private BufferedWriter streamOut = null;
	
	public AppClient(String serverName, int serverPort) throws UnknownHostException, IOException {
		System.out.println(ID + ": Establishing connection. Please wait ...");
		
		try {
			this.socket = new Socket(serverName, serverPort);
			this.ID = socket.getLocalPort();
			System.out.println(ID + ": Connected to server: " + socket.getInetAddress());
			System.out.println(ID + ": Connected to portid: " + socket.getLocalPort());
			this.start();
		} catch(UnknownHostException e) {
			System.err.println(ID + ": Unknown Host");
			throw e;
		} catch(IOException e) {
			System.err.println(ID + ": Unexpected exception");
			throw e;
		}
	}
	
	public int getID() {
		return this.ID;
	}
	
	public void handle(String msg) {
		if(msg.equals("quit")) {
			System.out.println(ID + "Good bye. Press RETURN to exit ...");
			stop();
		} else {
			System.out.println(msg);
		}
	}

	public void run() {
		System.out.println(ID + ": Client Started ...");
		while(streamOut != null) {
			try {
				if(thread != null) {
					streamOut.flush();
					streamOut.write(console.readLine() + "\n");
				} else {
					System.out.println(ID + ": Stream Closed");
				}
			} catch(IOException e) {
				System.out.println(ID + ": Sending error");
				stop();
			}
		}
	}
	
	public void start() throws IOException {
		try {
			console = new BufferedReader(new InputStreamReader(System.in));
			streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			streamOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			if(thread == null) {
//				client = new ClientThread(this, socket);
				thread = new Thread(this);
				thread.start();
			}
		} catch(IOException e) {
			System.out.println(ID + ": Error opening Data Input Stream");
			throw e;
		}
	}
	
	public void stop() {
		try {
			if(thread != null)
				thread = null;
			if(socket != null)
				socket.close();
			if(console != null)
				console.close();
			if(streamIn != null)
				streamIn.close();
			if(streamOut != null)
				streamOut.close();
			
			socket = null;
			console = null;
			streamIn = null;
			streamOut = null;
		} catch(IOException e) {
			System.out.println(ID + ": Error closing connection ...");
		}
		client.close();
	}
	
	public static void main(String args[]) {
		AppClient client = null;
		try {
			client = new AppClient(Config.DEFAULT_HOST, Config.DEFAULT_PORT);
		} catch(IOException e) {
			System.exit(0);
		}
	}

}

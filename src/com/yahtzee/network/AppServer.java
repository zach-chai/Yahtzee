package com.yahtzee.network;

import java.io.IOException;
import java.net.*;

import com.yahtzee.model.Combination;
import com.yahtzee.model.Dice;
import com.yahtzee.model.GameScore;
import com.yahtzee.model.MainDice;
import com.yahtzee.model.Player;
import com.yahtzee.utils.Config;

public class AppServer implements Runnable {
	
	int clientCount = 0;
	private Thread thread = null;
	private ServerSocket server = null;
	private ServerThread clients[] = new ServerThread[Config.MAX_CLIENTS];
	private GameScore gameScore;
	
	public AppServer(int port) {
		gameScore = new GameScore();
		try {
			System.out.println("Binding to port " + port + ", please wait ...");
			server = new ServerSocket(port);
			server.setReuseAddress(true);
			start();
		} catch(IOException e) {
			
		}
	}

	public void run() {
		while(thread != null) {
			try {
				System.out.println("Waiting for a client ...");
				addThread(server.accept());
			} catch(IOException e) {
				System.out.println("Server: Accepting Client Error");
//				Trace.exception(e);
			}
		}
	}
	
	public synchronized void handle(int ID, Object input) {
		if(input == null)
			return;
		ServerThread client = clients[findClient(ID)];
		if(input instanceof String) {
			String str = (String) input;
			if(str.equals("quit")) {
				System.out.println("Removing Client: " + ID);
				int pos = findClient(ID);
				if(pos != -1) {
					clients[pos].send("quit" + "\n");
					remove(ID);
				}
			} else if("round started".equals(str)) {
				if(clientCount >= 1) { //change this to 2 to prevent single player
					for(int i = 0; i < clientCount; i++) {
						clients[i].startRound();
					}
				} else {
					client.send("not enough players");
				}
			} else {
				for(int i = 0; i < clientCount; i++) {
					clients[i].send(ID + ": " + str + "\n");
				}
			}
		} else if(input instanceof Player) {
			System.out.println("YAY Player");
			Player player = (Player) input;
			player.moveDice();
			System.out.println("Combi: "+player.getMainDice().getCombination());
			System.out.println(player.getMainDice().getDice().toString());
			if(Combination.verify(player.getMainDice())) {
				System.out.println("verified");
				if(gameScore.calculateScore(player.getMainDice()) >= 0) {
					System.out.println("score added");
					client.ready();
					client.endRound();
					for(int i = 0; i < clientCount; i++) {
						clients[i].send(gameScore);
					}
					startRoundIfAllReady();
				} else {
					client.send("Invalid: score already taken");
				}
			} else {
				client.send("Invalid: score");
			}
			player = null;
		} else {
			System.out.println("Unknown object");
		}	
	}
	
	public void startRoundIfAllReady() {
		boolean allReady = true;
		
		for(int i = 0; i < clientCount; i++) {
			if(!clients[i].isReady())
				allReady = false;
		}
		
		if(allReady) {
			for(int i = 0; i < clientCount; i++) {
				clients[i].startRound();
			}
		}
	}
	
	public synchronized void remove(int ID) {
		int pos = findClient(ID);
		if(pos >= 0) {
			ServerThread toTerminate = clients[pos];
			System.out.println("Removing client thread " + ID + " at " + pos);
			if(pos < clientCount - 1) {
				for(int i = pos + 1; i < clientCount; i++) {
					clients[i - 1] = clients[i];
				}
			}
		}
		clientCount--;
	}
	
	public void start() {
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
			System.out.println("Server started: " + server + ": " + thread.getId());
		}
	}
	
	public void stop() {
		try {
			if(thread != null) {
				thread.join();
				thread = null;
			}
		} catch (InterruptedException e) {
//			Trace.exception(e);
		}
	}
	
	private int findClient(int ID) {
		for(int i = 0; i < clientCount; i++) {
			if(clients[i].getID() == ID) {
				return i;
			}
		}
		return -1;
	}
	
	private void addThread(Socket socket) {
		if (clientCount < clients.length) {
			System.out.println("Client accepted" + socket + "\n");
			clients[clientCount] = new ServerThread(this, socket);
			
			try {
				clients[clientCount].open();
				clients[clientCount].start();
				clientCount++;
				for(int i = 0; i < clientCount; i++) {
					clients[i].send("Player "+clientCount+" joined");
				}
			} catch (IOException e) {
				System.out.println("Error opening thread: ");
//				Trace.exception(e);
			}
		} else {
			System.out.println("Client refused maximum " + clients.length + " reached.");
		}
	}
	
	public static void main(String args[]) {
		AppServer server = new AppServer(Config.DEFAULT_PORT);
	}

}

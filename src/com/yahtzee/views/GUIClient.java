package com.yahtzee.views;

import java.applet.Applet;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.yahtzee.model.Player;
import com.yahtzee.network.ClientThread;
import com.yahtzee.utils.Config;

public class GUIClient extends Applet {
	private static final long serialVersionUID = -3311568690970149278L;
	
	private Player player;
	private ClientThread clientThread = null;
	private Socket socket = null;
	private BufferedReader streamIn = null;
	private BufferedWriter streamOut = null;
	private String serverPort;
	private String serverName;
	
	private TextField input;
	private TextArea display;
	private Button exit;
	private Button connect;
	private Button rollDice;
	private Button send;
	private Button diceButtons[];
	private Label label;
	
//	public GUIClient() {
//		
//		serverName = Config.DEFAULT_HOST;
//		serverPort = Config.DEFAULT_PORT+"";
//		
//		player = new Player();
//		
//		input = new TextField();
//		display = new TextArea();
//		
//		exit = new Button("Exit");
//		send = new Button("Send");
//		connect = new Button("Connect");
//		rollDice = new Button("Roll Dice");
//		
//		init();
//	}
	
	public void init() {
		
		serverName = Config.DEFAULT_HOST;
		serverPort = Config.DEFAULT_PORT+"";
		
		player = new Player();
		
		input = new TextField();
		display = new TextArea();
		
		exit = new Button("Exit");
		send = new Button("Send");
		connect = new Button("Connect");
		rollDice = new Button("Roll Dice");
		
		diceButtons = new Button[Config.MAX_DICE];
		for(int i = 0; i < diceButtons.length; i++) {
			diceButtons[i] = new Button(i+"");
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension btDim = new Dimension(85, 25);
		Dimension diceDim = new Dimension(40, 40);
		
		exit.setPreferredSize(btDim);
		send.setPreferredSize(btDim);
		connect.setPreferredSize(btDim);
		rollDice.setPreferredSize(btDim);
		
		for(Button b: diceButtons) {
			b.setPreferredSize(diceDim);
		}
		
		Panel buttons = new Panel();
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttons.add(connect);
		buttons.add(exit);
		buttons.add(send);
		buttons.add(rollDice);
		
		label = new Label("Yahtzee", Label.CENTER);
		label.setFont(new Font("Helvetica", Font.BOLD, 14));
		label.setSize(350, 20);
		
		Panel title = new Panel();
		title.setSize(350, 20);
		title.setLayout(new FlowLayout(FlowLayout.CENTER));
		title.add(label);
		
		Panel rollingDice = new Panel();
		rollingDice.setLayout(new FlowLayout(FlowLayout.CENTER));
		for(Button b: diceButtons) {
			rollingDice.add(b);
		}
		
		Panel south = new Panel();
		south.setLayout(new GridLayout(3, 1));
		south.add(input);
		south.add(rollingDice);
		south.add(buttons);
		
		setLayout(new BorderLayout());
		add("North", title);
		add("Center", display);
		add("South", south);
		this.setSize((int) screenSize.getWidth() / 5, (int) screenSize.getHeight() / 3);
		
		exit.setEnabled(false);
		send.setEnabled(false);
		connect.setEnabled(true);
		rollDice.setEnabled(false);
		
	}
	
	public boolean action(Event e, Object o) {
		if(e.target == exit) {
			input.setText("quit");
			display.setText("");
			display.append("Game Over" + "\n");
			
			exit.setEnabled(false);
			send.setEnabled(false);
			connect.setEnabled(false);
			rollDice.setEnabled(false);
			display.setEnabled(false);
			input.setEnabled(false);
			
			send();
			
			player = null;
		} else if(e.target == connect) {
			connect();
		} else if (e.target == send) {
			send();
			input.requestFocus();
		} else if (e.target == rollDice) {
//			this.player.rollDice();
//			this.refresh();
		}
		
		return true;
	}
	
	public void send() {
		try {
			streamOut.write(input.getText());
			streamOut.newLine();
			streamOut.flush();
			input.setText("");
		} catch (IOException e) {
			displayMsg("Error sending message");
			disconnect();
		}
	}
	
	public void handle(String msg) {
		if(msg.equals("quit")) {
			System.out.println("Good bye. Press RETURN to exit ...");
			stop();
		} else {
			System.out.println(msg);
		}
	}
	
	public void connect() {
		try {
			this.socket = new Socket(serverName, Integer.parseInt(serverPort));
			this.openStreams();
			clientThread = new ClientThread(this, socket);
			this.send.setEnabled(true);
			this.exit.setEnabled(true);
			this.rollDice.setEnabled(true);
			this.connect.setEnabled(false);
//			this.label.setText(player.getName() + " on Port: " + socket.getLocalPort());
			displayMsg("Connected");
		} catch(IOException e) {
			displayMsg("Connect error: " + e);
		}
	}
	
	public void disconnect() {
		try {
			if(streamIn != null)
				streamIn.close();
			if(streamOut != null)
				streamOut.close();
			if(socket != null)
				socket.close();
			
			socket = null;
			streamIn = null;
			streamOut = null;
		} catch(IOException e) {
			displayMsg("Error disconnecting");
		}
		clientThread.close();
	}
	
	public void openStreams() {
		try {
			streamIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			streamOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch(IOException e) {
			displayMsg("Error opening streams");
		}
	}
	
	public void displayMsg(String msg) {
		display.append(msg + "\n");
	}

}

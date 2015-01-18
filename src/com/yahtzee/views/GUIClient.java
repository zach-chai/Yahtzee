package com.yahtzee.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.yahtzee.model.Player;
import com.yahtzee.network.ClientThread;
import com.yahtzee.utils.Config;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUIClient extends JApplet {
	public GUIClient() {
	}
	private static final long serialVersionUID = -3311568690970149278L;
	
	private Player player;
	private ClientThread clientThread = null;
	private Socket socket = null;
	private BufferedReader streamIn = null;
	private BufferedWriter streamOut = null;
	private String serverPort;
	private String serverName;
	
	private JPanel south;
	private JPanel rolling;
	private JPanel saved;
	private JPanel buttons;
	
	private JTextField input;
	private JTextArea display;
	private JButton exit;
	private JButton connect;
	private JButton rollDice;
	private JButton send;
	private ArrayList<JButton> rollingDiceButtons;
	private ArrayList<JButton> savedDiceButtons;
	private JLabel label;
	private JLabel mainDice;
	private JLabel heldDice;
	
	public void init() {
		
		serverName = Config.DEFAULT_HOST;
		serverPort = Config.DEFAULT_PORT+"";
		
		player = new Player();
		
		rollingDiceButtons = new ArrayList<JButton>();		
		savedDiceButtons = new ArrayList<JButton>();
		
		input = new JTextField();
		display = new JTextArea();
		
		exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
			}
		});
		send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		connect = new JButton("Connect");
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connect();
			}
		});
		rollDice = new JButton("Roll Dice");
		rollDice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.rollDice();
				refreshDice();
			}
		});

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension btDim = new Dimension(85, 25);
		Dimension diceDim = new Dimension(50, 50);
		
		exit.setPreferredSize(btDim);
		send.setPreferredSize(btDim);
		connect.setPreferredSize(btDim);
		rollDice.setPreferredSize(btDim);
		
		buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttons.add(connect);
		buttons.add(exit);
		buttons.add(send);
		buttons.add(rollDice);
		
		label = new JLabel("Yahtzee", JLabel.CENTER);
		label.setFont(new Font("Helvetica", Font.BOLD, 14));
		label.setSize(350, 20);
		
		mainDice = new JLabel("Dice");
		heldDice = new JLabel("Held Dice");
		
		JPanel title = new JPanel();
		title.setSize(350, 20);
		title.setLayout(new FlowLayout(FlowLayout.CENTER));
		title.add(label);
		
		this.rolling = new JPanel();
		this.rolling.setLayout(new FlowLayout(FlowLayout.CENTER));
		rolling.add(mainDice);
		for(int i = 0; i < Config.MAX_DICE; i++) {
			JButton b = new JButton("");
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					player.getSavedDice().addDie(player.getMainDice().removeDie(player.getMainDice().getDice().get(rollingDiceButtons.indexOf(b))));
					refreshDice();
				}
			});
			b.setPreferredSize(diceDim);
			b.setEnabled(false);
			this.rollingDiceButtons.add(b);
			this.rolling.add(b);
		}

		saved = new JPanel();
		saved.setLayout(new FlowLayout(FlowLayout.CENTER));
		saved.add(heldDice);
		for(int i = 0; i < Config.MAX_DICE; i++) {
			JButton b = new JButton("");
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					player.getMainDice().addDie(player.getSavedDice().removeDie(player.getSavedDice().getDice().get(rollingDiceButtons.indexOf(b))));
					refreshDice();
				}
			});
			b.setPreferredSize(diceDim);
			b.setEnabled(false);
			this.savedDiceButtons.add(b);
			this.saved.add(b);
		}
		
		south = new JPanel();
		south.setLayout(new GridLayout(4, 1));
		south.add(input);
		south.add(rolling);
		
		
		
		south.add(saved);
		
		
		
		south.add(buttons);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("North", title);
		getContentPane().add("Center", display);
		getContentPane().add("South", south);
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
			this.player.rollDice();
			this.refreshDice();
		} 

		for(JButton b: rollingDiceButtons) {
			if(e.target == b) {
				this.player.getSavedDice().addDie(this.player.getMainDice().removeDie(this.player.getMainDice().getDice().get(rollingDiceButtons.indexOf(b))));
				this.refreshDice();
			}
		}
		
		return true;
	}
	
	public void refreshDice() {		
		for(int i = 1; i < rolling.getComponentCount(); i++) {
			((JButton) rolling.getComponent(i)).setText("");
			rolling.getComponent(i).setEnabled(false);
		}
		
		for(int i = 1; i < saved.getComponentCount(); i++) {
			((JButton) saved.getComponent(i)).setText("");
			saved.getComponent(i).setEnabled(false);
		}

		for(int i = 0; i < player.getMainDice().size(); i++) {
			rollingDiceButtons.get(i).setText((this.player.getMainDice().getDice().get(i).toString()));
			rollingDiceButtons.get(i).setEnabled(true);
		}

		for(int i = 0; i < player.getSavedDice().size(); i++) {
			savedDiceButtons.get(i).setText((this.player.getSavedDice().getDice().get(i).toString()));
			savedDiceButtons.get(i).setEnabled(true);
		}
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

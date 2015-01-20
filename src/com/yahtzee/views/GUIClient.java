package com.yahtzee.views;

import java.awt.BorderLayout;
import java.awt.Container;
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.yahtzee.model.Combination;
import com.yahtzee.model.GameScore;
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
	private ObjectOutputStream streamOut = null;
	private String serverPort;
	private String serverName;
	
	private JPanel scoreBoard;
	private JPanel south;
	private JPanel rolling;
	private JPanel saved;
	private JPanel buttons;
	private JPanel game;
	private JPanel upperScore;
	private JPanel lowerScore;
	
	private JTextField input;
	private JTextArea display;
	
	private JButton exit;
	private JButton connect;
	private JButton rollDice;
	private JButton send;
	private JButton upperButtons[];
	private JButton lowerButtons[];
	
	private ArrayList<JButton> rollingDiceButtons;
	private ArrayList<JButton> savedDiceButtons;
	
	private JLabel label;
	private JLabel scoreLabels[];
	private JLabel mainDiceLabel;
	private JLabel heldDiceLabel;
	
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
				send();
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
				for(JButton b: upperButtons) {
					b.setEnabled(true);
				}
				for(JButton b: lowerButtons) {
					b.setEnabled(true);
				}
			}
		});
		
		upperButtons = new JButton[] {
				new JButton("Aces"),
				new JButton("Twos"),
				new JButton("Threes"),
				new JButton("Fours"),
				new JButton("Fives"),
				new JButton("Sixes"),
				new JButton("Chance")
			};
		
		upperButtons[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.ACES);
				send(player);
			}
		});
		upperButtons[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.TWOS);
				send(player);
			}
		});
		upperButtons[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.THREES);
				send(player);
			}
		});
		upperButtons[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.FOURS);
				send(player);
			}
		});
		upperButtons[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.FIVES);
				send(player);
			}
		});
		upperButtons[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.SIXES);
				send(player);
			}
		});
		upperButtons[6].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.CHANCE);
				send(player);
			}
		});
		
		lowerButtons = new JButton[]{
				new JButton("3 of Kind"),
				new JButton("4 of Kind"),
				new JButton("Full House"),
				new JButton("Sm. Straight"),
				new JButton("Lg. Straight"),
				new JButton("Yahtzee")
		};
		
		lowerButtons[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.THREE_OF_KIND);
				send(player);
			}
		});
		lowerButtons[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.FOUR_OF_KIND);
				send(player);
			}
		});
		lowerButtons[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.FULL_HOUSE);
				send(player);
			}
		});
		lowerButtons[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.SMALL_STRAIGHT);
				send(player);
			}
		});
		lowerButtons[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.LARGE_STRAIGHT);
				send(player);
			}
		});
		lowerButtons[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.getMainDice().setCombination(Combination.YAHTZEE);
				send(player);
			}
		});
		
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
		
		scoreLabels = new JLabel[] {
				new JLabel("Upper", JLabel.CENTER),
				new JLabel("Game 1", JLabel.CENTER),
				new JLabel("Game 2", JLabel.CENTER),
				new JLabel("Game 3", JLabel.CENTER),
				new JLabel("Game 4", JLabel.CENTER),
				new JLabel("Game 5", JLabel.CENTER),
				new JLabel("Game 6", JLabel.CENTER),
				
				new JLabel("Aces", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Twos", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Threes", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Fours", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Fives", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Sixes", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Total", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Bonus", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Total + Bonus", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("3 of a kind", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("4 of a kind", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Full house", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Sm. Straight", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Lg. Straight", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Yahtzee", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Chance", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Yahtzee Bonus", JLabel.CENTER),
				new JLabel(" | | ", JLabel.CENTER),
				new JLabel(" | | ", JLabel.CENTER),
				new JLabel(" | | ", JLabel.CENTER),
				new JLabel(" | | ", JLabel.CENTER),
				new JLabel(" | | ", JLabel.CENTER),
				new JLabel(" | | ", JLabel.CENTER),
				
				new JLabel("Total", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				
				new JLabel("Grand Total", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
				new JLabel("", JLabel.CENTER),
		};
		
		mainDiceLabel = new JLabel("Main Dice");
		heldDiceLabel = new JLabel("Held Dice");
		mainDiceLabel.setSize(200, 20);
		heldDiceLabel.setSize(200, 20);
		
		JPanel title = new JPanel();
		title.setSize(350, 20);
		title.setLayout(new FlowLayout(FlowLayout.CENTER));
		title.add(label);
		
		this.rolling = new JPanel();
		this.rolling.setLayout(new FlowLayout(FlowLayout.CENTER));
		rolling.add(mainDiceLabel);
		for(int i = 0; i < Config.MAX_DICE; i++) {
			JButton b = new JButton("");
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					player.getSavedDice().addDie(player.getMainDice().removeDie(player.getMainDice().getDice().get(rollingDiceButtons.indexOf(e.getSource()))));
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
		saved.add(heldDiceLabel);
		for(int i = 0; i < Config.MAX_DICE; i++) {
			JButton b = new JButton("");
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					player.getMainDice().addDie(player.getSavedDice().removeDie(player.getSavedDice().getDice().get(savedDiceButtons.indexOf(e.getSource()))));
					refreshDice();
				}
			});
			b.setPreferredSize(diceDim);
			b.setEnabled(false);
			this.savedDiceButtons.add(b);
			this.saved.add(b);
		}
		
		upperScore = new JPanel();
		upperScore.setLayout(new FlowLayout(FlowLayout.CENTER));
		for(JButton b: upperButtons) {
			upperScore.add(b);
		}
		
		lowerScore = new JPanel();
		lowerScore.setLayout(new FlowLayout(FlowLayout.CENTER));
		for(JButton b: lowerButtons) {
			lowerScore.add(b);
		}
		
		south = new JPanel();
		south.setLayout(new GridLayout(6, 1));
		south.add(input);
		south.add(rolling);
		south.add(saved);
		south.add(buttons);
		south.add(upperScore);
		south.add(lowerScore);
		
		scoreBoard = new JPanel();
		scoreBoard.setLayout(new GridLayout(21, 7));
		for(JLabel l: scoreLabels) {
			scoreBoard.add(l);
		}
		
		game = new JPanel();
		game.setLayout(new BorderLayout());
		game.add(title, BorderLayout.NORTH);
		game.add(display, BorderLayout.CENTER);
		game.add(south, BorderLayout.SOUTH);
		
		Container container = getContentPane();
		container.setLayout(new GridLayout(1, 2));
		container.add(game);
		container.add(scoreBoard);
		
		this.setSize(1200, 500);
		
		exit.setEnabled(false);
		send.setEnabled(false);
		connect.setEnabled(true);
		rollDice.setEnabled(false);
		for(JButton b: upperButtons) {
			b.setEnabled(false);
		}
		for(JButton b: lowerButtons) {
			b.setEnabled(false);
		}
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
	
	public void updateScoreBoard(GameScore gameScores) {
		int[] scores = gameScores.toArray();
		int i = 8;
		for(int j = 0; j < scores.length; j++) {
			if(i == 50 || i == 64) {
				i += 7;
			}
			if(scores[j] != 0)
				scoreLabels[i].setText(scores[j]+"");
			i += 7;
		}
		scoreLabels[i].setText(gameScores.yahtzeeBonusToString());
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
			streamOut.writeObject(input.getText());
			streamOut.flush();
			input.setText("");
		} catch (IOException e) {
			displayMsg("Error sending message");
			disconnect();
		}
	}
	
	public void send(Object input) {
		displayMsg("sending object");
		System.out.println(player.getMainDice().getCombination());
		try {
			streamOut.writeObject(input);
			streamOut.flush();
			streamOut.reset();
		} catch (IOException e) {
			displayMsg("Error sending object");
			disconnect();
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
			if(streamOut != null)
				streamOut.close();
			if(socket != null)
				socket.close();
			
			socket = null;
			streamOut = null;
		} catch(IOException e) {
			displayMsg("Error disconnecting");
		}
		clientThread.close();
	}
	
	public void openStreams() {
		System.out.println("opening stream...");
		try {
			streamOut = new ObjectOutputStream(socket.getOutputStream());
			streamOut.flush();
			System.out.println("stream opened");
		} catch(IOException e) {
			displayMsg("Error opening streams");
		}
	}
	
	public void displayMsg(String msg) {
		display.append(msg + "\n");
	}

}

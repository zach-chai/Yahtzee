package com.yahtzee.views;

import javax.swing.JTextField;
import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ConnectionPane extends JPanel {
	private JTextField port;
	private JTextField host;
	public ConnectionPane(String defaultHost, String defaultPort) {
		setLayout(new GridLayout(4, 1));
		
		JLabel lblNewLabel = new JLabel("Host");
		add(lblNewLabel);
		
		host = new JTextField(defaultHost);
		add(host);
		host.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Port");
		add(lblNewLabel_1);
		
		port = new JTextField(defaultPort);
		add(port);
		port.setColumns(10);
		
		
	}
	
	public String getHost() {
		return host.getText();
	}
	
	public String getPort() {
		return port.getText();
	}

}

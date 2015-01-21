package com.yahtzee.model;

import java.io.Serializable;

public class Player implements Serializable {
	private static final long serialVersionUID = -5399182036152162397L;
	
	MainDice mainDice;
	SavedDice savedDice;
	int score;
	
	public Player() {
		mainDice = new MainDice();
		savedDice = new SavedDice();
	}
	
	public void rollDice() {
		mainDice.roll();
	}

	public MainDice getMainDice() {
		return mainDice;
	}

	public SavedDice getSavedDice() {
		return savedDice;
	}
	
	public void clearDice() {
		mainDice.clear();
		savedDice.clear();
	}
	
	public void moveDice() {
		while(savedDice.size() > 0) {
			mainDice.addDie(savedDice.removeDie(savedDice.getDice().get(0)));
		}
	}
	

}

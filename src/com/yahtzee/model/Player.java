package com.yahtzee.model;

import java.io.Serializable;

public class Player implements Serializable {
	
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
	

}

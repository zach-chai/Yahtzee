package com.yahtzee.model;

public class MainDice extends Dice {
	
	//constants
	private static final int INITIAL_DICE = 5;
	
	public MainDice() {
		loadDice();
	}
	
	public void loadDice() {
		for(int i = 0; i < INITIAL_DICE; i++) {
			this.addDie();
		}
	}
	
	public void roll() {
		for(Die d: dice) {
			d.roll();
		}
	}

}

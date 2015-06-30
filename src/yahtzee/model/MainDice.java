package com.yahtzee.model;

import com.yahtzee.utils.Config;

public class MainDice extends Dice {
	
	private int combination;
	
	public MainDice() {
		reloadDice();
	}
	
	public void reloadDice() {
		this.dice.clear();
		for(int i = 0; i < Config.MAX_DICE; i++) {
			this.addDie();
		}
	}
	
	public void roll() {
		for(Die d: dice) {
			d.roll();
		}
	}

	public int getCombination() {
		return combination;
	}

	public void setCombination(int combination) {
		this.combination = combination;
	}

}

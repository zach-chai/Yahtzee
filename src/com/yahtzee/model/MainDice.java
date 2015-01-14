package com.yahtzee.model;

import com.yahtzee.utils.Config;

public class MainDice extends Dice {
	
	public MainDice() {
		loadDice();
	}
	
	public void loadDice() {
		for(int i = 0; i < Config.MAX_DICE; i++) {
			this.addDie();
		}
	}
	
	public void roll() {
		for(Die d: dice) {
			d.roll();
		}
	}

}

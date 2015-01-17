package com.yahtzee.model;

import java.util.ArrayList;

public abstract class Dice {

	protected ArrayList<Die> dice = new ArrayList<Die>();

	public Die removeDie(Die die) {
		Die d = die;
		this.dice.remove(die);
		return d;
	}
	
	public void addDie() {
		this.dice.add(new Die());
	}

	public void addDie(Die die) {
		this.dice.add(die);
	}
	
	public int size() {
		return this.dice.size();
	}

	public ArrayList<Die> getDice() {
		return this.dice;
	}

	public void setDice(ArrayList<Die> dice) {
		this.dice = dice;
	}
	
	public String toString() {
		String s = "|";
		for(Die d: dice) {
			s += d.toString() + "|";
		}
		return s;
	}

}

package com.yahtzee.model;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Dice implements Serializable {

	private static final long serialVersionUID = -2793273821696208509L;
	
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
	
	public boolean contains(Die die) {
		for(Die d: getDice()) {
			if(d.getValue() == die.getValue()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(int value) {
		for(Die d: getDice()) {
			if(value == d.getValue()) {
				return true;
			}
		}
		return false;
	}
	
	public int numDie(Die die) {
		int num = 0;
		for(Die d: getDice()) {
			if(d.getValue() == die.getValue()) {
				num++;
			}
		}
		return num;
	}
	
	public int numDie(int value) {
		int num = 0;
		for(Die d: getDice()) {
			if(d.getValue() == value) {
				num++;
			}
		}
		return num;
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

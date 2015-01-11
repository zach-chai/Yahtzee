package com.yahtzee.model;

import java.util.ArrayList;

public class Dice {
	
	private ArrayList<Die> dice = new ArrayList<Die>();
	
	public void removeDie(Die die){
		dice.remove(die);
	}
	
	public void addDie(Die die){
		dice.add(die);
	}

	public ArrayList<Die> getDice() {
		return dice;
	}

	public void setDice(ArrayList<Die> dice) {
		this.dice = dice;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

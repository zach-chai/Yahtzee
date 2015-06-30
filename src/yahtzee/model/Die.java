package com.yahtzee.model;

import java.io.Serializable;
import java.util.Random;

public class Die implements Serializable {

	private static final long serialVersionUID = 7485273716365407176L;

	private int value;
	
	//constants
	public static final int ACE = 1;
	public static final int TWO = 2;
	public static final int THREE = 3;
	public static final int FOUR = 4;
	public static final int FIVE = 5;
	public static final int SIX = 6;
	
	public Die() {
		value = SIX;
	}
	
	public Die(int v) {
		value = v;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public void roll() {
		Random rand = new Random();
		value = rand.nextInt(6) + 1;
	}
	
	public String toString() {
		return value+"";
	}

}

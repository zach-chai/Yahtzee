package com.yahtzee.model;

import java.util.Random;

public class Die {
	
	private int value;
	
	//constants
	private static final int ONE = 1;
	private static final int TWO = 2;
	private static final int THREE = 3;
	private static final int FOUR = 4;
	private static final int FIVE = 5;
	private static final int SIX = 6;
	
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

}

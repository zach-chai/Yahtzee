package com.yahtzee.model;

import java.util.HashSet;
import java.util.Set;

public final class Combination {
	
	public static final int YAHTZEE = 0;
	public static final int ACES = 1;
	public static final int TWOS = 2;
	public static final int THREES = 3;
	public static final int FOURS = 4;
	public static final int FIVES = 5;
	public static final int SIXES = 6;
	public static final int THREE_OF_KIND = 7;
	public static final int FOUR_OF_KIND = 8;
	public static final int FULL_HOUSE = 9;
	public static final int SMALL_STRAIGHT = 10;
	public static final int LARGE_STRAIGHT = 11;
	public static final int CHANCE = 12;
	
	public static boolean verify(MainDice dice) {
		switch(dice.getCombination()) {
		case ACES: return verifyAces(dice);
		case TWOS: return verifyTwos(dice);
		case THREES: return verifyThrees(dice);
		case FOURS: return verifyFours(dice);
		case FIVES: return verifyFives(dice);
		case SIXES: return verifySixes(dice);
		case THREE_OF_KIND: return verifyThreeOfKind(dice);
		case FOUR_OF_KIND: return verifyFourOfKind(dice);
		case FULL_HOUSE: return verifyFullHouse(dice);
		case SMALL_STRAIGHT: return verifySmallStraight(dice);
		case LARGE_STRAIGHT: return verifyLargeStraight(dice);
		case CHANCE: return true;
		case YAHTZEE: return verifyYahtzee(dice);
		}
		return false;
	}
	
	public static boolean verifyYahtzee(MainDice dice) {
		Set<Die> set = new HashSet<Die>(dice.getDice());
		return set.size() == 1;
	}
	
	public static boolean verifyLargeStraight(MainDice dice) {
		Set<Die> set = new HashSet<Die>(dice.getDice());
		if(dice.contains(1) && dice.contains(6))
			return false;
		return dice.size() == set.size();
	}
	
	public static boolean verifySmallStraight(MainDice dice) {
		if(!(dice.contains(3) && dice.contains(4)))
			return false;
		if(dice.contains(2) && dice.contains(5))
			return true;
		if(dice.contains(1) && dice.contains(2))
			return true;
		if(dice.contains(5) && dice.contains(6))
			return true;
		return false;
	}
	
	public static boolean verifyFullHouse(MainDice dice) {
		Set<Die> set = new HashSet<Die>(dice.getDice());
		if(set.size() != 2)
			return false;
		Die arr[] = set.toArray(new Die[set.size()]);
		return (dice.numDie(arr[0]) == 2 || dice.numDie(arr[0]) == 3);
	}
	
	public static boolean verifyFourOfKind(MainDice dice) {
		Set<Die> set = new HashSet<Die>(dice.getDice());
		if(set.size() != 2)
			return false;
		Die arr[] = set.toArray(new Die[set.size()]);
		return (dice.numDie(arr[0]) == 4 || dice.numDie(arr[1]) == 4);
	}
	
	public static boolean verifyThreeOfKind(MainDice dice) {
		Set<Die> set = new HashSet<Die>(dice.getDice());
		if(set.size() > 3)
			return false;
		Die arr[] = set.toArray(new Die[set.size()]);
		return (dice.numDie(arr[0]) == 3 || dice.numDie(arr[1]) == 3 || dice.numDie(arr[2]) == 3);
	}
	
	public static boolean verifyAces(MainDice dice) {
		return dice.contains(1);
	}
	
	public static boolean verifyTwos(MainDice dice) {
		return dice.contains(2);
	}
	
	public static boolean verifyThrees(MainDice dice) {
		return dice.contains(3);
	}
	
	public static boolean verifyFours(MainDice dice) {
		return dice.contains(4);
	}
	
	public static boolean verifyFives(MainDice dice) {
		return dice.contains(5);
	}
	
	public static boolean verifySixes(MainDice dice) {
		return dice.contains(6);
	}

}

package com.yahtzee.model;

import java.util.HashSet;
import java.util.Set;

public final class Combinations {
	
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

}
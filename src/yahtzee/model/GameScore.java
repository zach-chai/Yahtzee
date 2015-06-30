package com.yahtzee.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GameScore implements Serializable {
	
	private static final long serialVersionUID = -4567622799857113043L;
	
	private int aces;
	private int twos;
	private int threes;
	private int fours;
	private int fives;
	private int sixes;
	private int bonus;
	private int threeKind;
	private int fourKind;
	private int fullHouse;
	private int smallStraight;
	private int largeStraight;
	private int yahtzee;
	private int chance;
	private ArrayList<Integer> yahtzeeBonus;
	
	public GameScore() {
		aces = -1;
		twos = -1;
		threes = -1;
		fours = -1;
		fives = -1;
		sixes = -1;
		bonus = 0;
		threeKind = -1;
		fourKind = -1;
		fullHouse = -1;
		smallStraight = -1;
		largeStraight = -1;
		yahtzee = -1;
		chance = -1;
		yahtzeeBonus = new ArrayList<Integer>();
	}
	
	public int calculateScore(MainDice dice) {
		switch(dice.getCombination()) {
		case Combination.ACES:
			if(aces >= 0)
				return -1;
			return calculateAces(dice);
		case Combination.TWOS:
			if(twos >= 0)
				return -1;
			return calculateTwos(dice);
		case Combination.THREES:
			if(threes >= 0)
				return -1;
			return calculateThrees(dice);
		case Combination.FOURS:
			if(fours >= 0)
				return -1;
			return calculateFours(dice);
		case Combination.FIVES:
			if(fives >= 0)
				return -1;
			return calculateFives(dice);
		case Combination.SIXES:
			if(sixes >= 0)
				return -1;
			return calculateSixes(dice);
		case Combination.THREE_OF_KIND:
			if(threeKind >= 0)
				return -1;
			return calculateThreeKind(dice);
		case Combination.FOUR_OF_KIND:
			if(fourKind >= 0)
				return -1;
			return calculateFourKind(dice);
		case Combination.FULL_HOUSE:
			if(fullHouse >= 0)
				return -1;
			return calculateFullHouse(dice);
		case Combination.SMALL_STRAIGHT:
			if(smallStraight >= 0)
				return -1;
			return calculateSmallStraight(dice);
		case Combination.LARGE_STRAIGHT:
			if(largeStraight >= 0)
				return -1;
			return calculateLargeStraight(dice);
		case Combination.CHANCE:
			if(chance >= 0)
				return -1;
			return calculateChance(dice);
		case Combination.YAHTZEE:
			if(yahtzee > 0) {
				return calculateYahtzeeBonus(dice);
			} else if(yahtzee < 0) {
				return calculateYahtzee(dice);
			} else {
				return -1;
			}
		}
		return -1;
	}
	
	public int calculateChance(MainDice dice) {
		chance = 0;
		for(Die d: dice.getDice())
			chance += d.getValue();
		return chance;
	}

	public int calculateAces(MainDice dice) {
		if(Combination.verifyAces(dice)) {
			aces = dice.numDie(Die.ACE);
			calculateBonus(dice);
			return aces;
		} else {
			return aces = 0;
		}
	}
	
	public int calculateTwos(MainDice dice) {
		if(Combination.verifyTwos(dice)) {
			twos = dice.numDie(Die.TWO) * Die.TWO;
			calculateBonus(dice);
			return twos;
		} else {
			return twos = 0;
		}
	}
	
	public int calculateThrees(MainDice dice) {
		if(Combination.verifyThrees(dice)) {
			threes = dice.numDie(Die.THREE) * Die.THREE;
			calculateBonus(dice);
			return threes;
		} else {
			return threes = 0;
		}
	}
	
	public int calculateFours(MainDice dice) {
		if(Combination.verifyFours(dice)) {
			fours = dice.numDie(Die.FOUR) * Die.FOUR;
			calculateBonus(dice);
			return fours;
		} else {
			return fours = 0;
		}
	}
	
	public int calculateFives(MainDice dice) {
		if(Combination.verifyFives(dice)) {
			fives = dice.numDie(Die.FIVE) * Die.FIVE;
			calculateBonus(dice);
			return fives;
		} else {
			return fives = 0;
		}
	}
	
	public int calculateSixes(MainDice dice) {
		if(Combination.verifySixes(dice)) {
			sixes = dice.numDie(Die.SIX) * Die.SIX;
			calculateBonus(dice);
			return sixes;
		} else {
			return sixes = 0;
		}
	}
	
	public int calculateBonus(MainDice dice) {
		if((calculateUpperTotal()) >= 63) {
			bonus = 35;
			return bonus;
		} else {
			return 0;
		}
	}
	
	public int calculateThreeKind(MainDice dice) {
		if(Combination.verifyThreeOfKind(dice)) {
			threeKind = 0;
			for(Die d: dice.getDice())
				threeKind += d.getValue();
			return threeKind;
		} else {
			return threeKind = 0;
		}
	}
	
	public int calculateFourKind(MainDice dice) {
		if(Combination.verifyFourOfKind(dice)) {
			fourKind = 0;
			for(Die d: dice.getDice())
				fourKind += d.getValue();
			return fourKind;
		} else {
			return fourKind = 0;
		}
	}
	
	public int calculateFullHouse(MainDice dice) {
		if(Combination.verifyFullHouse(dice)) {
			return fullHouse = 25;
		} else {
			return fullHouse = 0;
		}
	}
	
	public int calculateSmallStraight(MainDice dice) {
		if(Combination.verifySmallStraight(dice)) {
			return smallStraight = 30;
		} else {
			return smallStraight = 0;
		}
	}
	
	public int calculateLargeStraight(MainDice dice) {
		if(Combination.verifyLargeStraight(dice)) {
			return largeStraight = 40;
		} else {
			return largeStraight = 0;
		}
	}
	
	public int calculateYahtzee(MainDice dice) {
		if(Combination.verifyYahtzee(dice)) {
			return yahtzee = 50;
		} else {
			return yahtzee = 0;
		}
	}
	
	public int calculateYahtzeeBonus(MainDice dice) {
		if(Combination.verifyYahtzee(dice)) {
			yahtzeeBonus.add(100);
			return 100;
		} else {
			return -1;
		}
	}
	
	public int getBonus() {
		return bonus;
	}
	
	public int calculateUpperTotal() {
		return aces + twos + threes + fours + fives + sixes;
	}
	
	public int calculateLowerTotal() {
		return threeKind + fourKind + smallStraight + largeStraight + fullHouse + yahtzee + chance + (yahtzeeBonus.size() * 100);
	}
	
	public boolean finishedScoring() {
		int arr[] = this.toArray();
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] == -1)
				return false;
		}
		return true;
	}
	
	public int[] toArray() {
		return new int[] {
				aces, twos, threes, fours, fives, sixes, bonus,
				threeKind, fourKind, fullHouse, smallStraight,
				largeStraight, yahtzee, chance
		};
	}
	
	public String yahtzeeBonusToString() {
		String s = "";
		Iterator<Integer> it = yahtzeeBonus.iterator();
		for(int i = 0; i < 3; i++) {
			if(it.hasNext()) {
				s += it.next();
			} else {
				s += " ";
			}
			s += "|";
		}
		return s;
	}

}

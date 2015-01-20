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
		aces = 0;
		twos = 0;
		threes = 0;
		fours = 0;
		fives = 0;
		sixes = 0;
		bonus = 0;
		threeKind = 0;
		fourKind = 0;
		fullHouse = 0;
		smallStraight = 0;
		largeStraight = 0;
		yahtzee = 0;
		chance = 0;
		yahtzeeBonus = new ArrayList<Integer>();
	}
	
	public int calculateScore(MainDice dice) {
		switch(dice.getCombination()) {
		case Combination.ACES:
			if(aces != 0)
				return -1;
			return calculateAces(dice);
		case Combination.TWOS:
			if(twos != 0)
				return -1;
			return calculateTwos(dice);
		case Combination.THREES:
			if(threes != 0)
				return -1;
			return calculateThrees(dice);
		case Combination.FOURS:
			if(fours != 0)
				return -1;
			return calculateFours(dice);
		case Combination.FIVES:
			if(fives != 0)
				return -1;
			return calculateFives(dice);
		case Combination.SIXES:
			if(sixes != 0)
				return -1;
			return calculateSixes(dice);
		case Combination.THREE_OF_KIND:
			if(threeKind != 0)
				return -1;
			return calculateThreeKind(dice);
		case Combination.FOUR_OF_KIND:
			if(fourKind != 0)
				return -1;
			return calculateFourKind(dice);
		case Combination.FULL_HOUSE:
			if(fullHouse != 0)
				return -1;
			return calculateFullHouse(dice);
		case Combination.SMALL_STRAIGHT:
			if(smallStraight != 0)
				return -1;
			return calculateSmallStraight(dice);
		case Combination.LARGE_STRAIGHT:
			if(largeStraight != 0)
				return -1;
			return calculateLargeStraight(dice);
		case Combination.CHANCE:
			if(chance != 0)
				return -1;
			return calculateChance(dice);
		case Combination.YAHTZEE:
			if(yahtzee != 0)
				return calculateYahtzeeBonus(dice);
			return calculateYahtzee(dice);
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
		aces = dice.numDie(Die.ACE);
		calculateBonus(dice);
		return aces;
	}
	
	public int calculateTwos(MainDice dice) {
		twos = dice.numDie(Die.TWO) * Die.TWO;
		calculateBonus(dice);
		return twos;
	}
	
	public int calculateThrees(MainDice dice) {
		threes = dice.numDie(Die.THREE) * Die.THREE;
		calculateBonus(dice);
		return threes;
	}
	
	public int calculateFours(MainDice dice) {
		fours = dice.numDie(Die.FOUR) * Die.FOUR;
		calculateBonus(dice);
		return fours;
	}
	
	public int calculateFives(MainDice dice) {
		fives = dice.numDie(Die.FIVE) * Die.FIVE;
		calculateBonus(dice);
		return fives;
	}
	
	public int calculateSixes(MainDice dice) {
		sixes = dice.numDie(Die.SIX) * Die.SIX;
		calculateBonus(dice);
		return sixes;
	}
	
	public int calculateBonus(MainDice dice) {
		if((aces + twos + threes + fours + fives + sixes) >= 63) {
			bonus = 35;
		}
		return bonus;
	}
	
	public int calculateThreeKind(MainDice dice) {
		threeKind = 0;
		for(Die d: dice.getDice())
			threeKind += d.getValue();
		return threeKind;
	}
	
	public int calculateFourKind(MainDice dice) {
		fourKind = 0;
		for(Die d: dice.getDice())
			fourKind += d.getValue();
		return fourKind;
	}
	
	public int calculateFullHouse(MainDice dice) {
		return fullHouse = 25;
	}
	
	public int calculateSmallStraight(MainDice dice) {
		return smallStraight = 30;
	}
	
	public int calculateLargeStraight(MainDice dice) {
		return largeStraight = 40;
	}
	
	public int calculateYahtzee(MainDice dice) {
		return yahtzee = 50;
	}
	
	public int calculateYahtzeeBonus(MainDice dice) {
		yahtzeeBonus.add(100);
		return 100;
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

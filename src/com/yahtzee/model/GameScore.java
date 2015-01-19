package com.yahtzee.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GameScore {
	
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
		yahtzeeBonus = new ArrayList<Integer>();
	}
	
	public boolean calculateScore(MainDice dice) {
		switch(dice.getCombination()) {
		case Combination.ACES: calculateAces(dice);
		case Combination.TWOS: calculateTwos(dice);
		case Combination.THREES: calculateThrees(dice);
		case Combination.FOURS: calculateFours(dice);
		case Combination.FIVES: calculateFives(dice);
		case Combination.SIXES: calculateSixes(dice);
		case Combination.THREE_OF_KIND: calculateThreeKind(dice);
		case Combination.FOUR_OF_KIND: calculateFourKind(dice);
		case Combination.FULL_HOUSE: calculateFullHouse(dice);
		case Combination.SMALL_STRAIGHT: calculateSmallStraight(dice);
		case Combination.LARGE_STRAIGHT: calculateLargeStraight(dice);
		case Combination.CHANCE: calculateChance(dice);
		case Combination.YAHTZEE: calculateYahtzee(dice);
		}
		return false;
	}
	
	public void calculateChance(MainDice dice) {
		chance = 0;
		for(Die d: dice.getDice())
			chance += d.getValue();
	}

	public void calculateAces(MainDice dice) {
		aces = dice.numDie(Die.ACE);
		calculateBonus(dice);
	}
	
	public void calculateTwos(MainDice dice) {
		twos = dice.numDie(Die.TWO) * Die.TWO;
		calculateBonus(dice);
	}
	
	public void calculateThrees(MainDice dice) {
		threes = dice.numDie(Die.THREE) * Die.THREE;
		calculateBonus(dice);
	}
	
	public void calculateFours(MainDice dice) {
		fours = dice.numDie(Die.FOUR) * Die.FOUR;
		calculateBonus(dice);
	}
	
	public void calculateFives(MainDice dice) {
		fives = dice.numDie(Die.FIVE) * Die.FIVE;
		calculateBonus(dice);
	}
	
	public void calculateSixes(MainDice dice) {
		sixes = dice.numDie(Die.SIX) * Die.SIX;
		calculateBonus(dice);
	}
	
	public void calculateBonus(MainDice dice) {
		if((aces + twos + threes + fours + fives + sixes) >= 63) {
			bonus = 35;
		}
	}
	
	public void calculateThreeKind(MainDice dice) {
		threeKind = 0;
		for(Die d: dice.getDice())
			threeKind += d.getValue();
	}
	
	public void calculateFourKind(MainDice dice) {
		fourKind = 0;
		for(Die d: dice.getDice())
			fourKind += d.getValue();
	}
	
	public void calculateFullHouse(MainDice dice) {
		fullHouse = 25;
	}
	
	public void calculateSmallStraight(MainDice dice) {
		smallStraight = 30;
	}
	
	public void calculateLargeStraight(MainDice dice) {
		largeStraight = 40;
	}
	
	public void calculateYahtzee(MainDice dice) {
		yahtzee = 50;
	}
	
	public void calculateYahtzeeBonus(MainDice dice) {
		yahtzeeBonus.add(100);
	}

}

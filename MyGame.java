package yahtzee;

import gameNet.GameNet_CoreGame;

import java.io.Serializable;
import java.util.ArrayList;

//start inner class dice
class Dice implements Serializable
{
	public int value;
	public boolean keep;

	Dice()
	{
		value = 0;
		keep = false;
	}
}

// end inner class dice

// start inner class player
class Player implements Serializable
{
	public String name;
	public int[] score;
	public int total;

	Player()
	{
		name = "NO NAME";
		score = new int[13];

		for (int i = 0; i < score.length; i++)
		{
			score[i] = 0;
		}

		total = 0;
	}

	Player(String name)
	{
		this.name = name;
		score = new int[13];
		score = initPlayerScores();
		total = 0;
	}
	
	public int[] initPlayerScores()
	{
		int[] initScores = new int[13];
		for(int i = 0; i<score.length; i++)
		{
			initScores[i] = 0;
		}
		return initScores;
	}
}

// end inner class player

public class MyGame extends GameNet_CoreGame implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final int MAX_PLAYERS = 2;
	private ArrayList<Player> currPlayers = new ArrayList<Player>();
	private Dice[] dice = new Dice[5];
	private YahtzeeUtil yu = new YahtzeeUtil();
	private int score = 0;
	//private int rollCount = 0;

	public Object process(Object inputs)
	{
		MyGameInput myGameInput = (MyGameInput) inputs;
		int playerIndex = getPlayerIndex(myGameInput.myName);

		if (playerIndex < 0)
		{
			System.out.println("Already have max amount of players");
			return null;
		}

		switch (myGameInput.command)
		{
		case MyGameInput.JOIN_GAME:
			dice = yu.initializeDice(dice);
			break;
		case MyGameInput.ROLL_DICE:
				dice = yu.rollDice(playerIndex, dice);
				//rollCount+=1;
			break;
		case MyGameInput.KEEP_DIE:
			dice = yu.setKeep(playerIndex, myGameInput.index, dice);
			break;
		case MyGameInput.RELEASE_DIE:
			dice = yu.setRelease(playerIndex, myGameInput.index, dice);
			break;
		case MyGameInput.CHECK_SCORE:
			score = yu.checkScore(myGameInput.index, dice);
					currPlayers.get(playerIndex).score[myGameInput.index] = score;
					currPlayers.get(playerIndex).total = updatePlayerScore(playerIndex);
					//rollCount = 0;

		case MyGameInput.DISCONNECTING:
			currPlayers.remove(myGameInput.myName);
		case MyGameInput.RESETTING:
			break;
		default:
		}

		MyGameOutput myGameOutput = new MyGameOutput(this);
		return myGameOutput;
	}

	private int getPlayerIndex(String name)
	{
		int playerIndex = -1;

		for (int i = 0; i < currPlayers.size(); i++)
		{
			if (name.equals(currPlayers.get(i).name))
				return i;
		}

		if (currPlayers.size() < MAX_PLAYERS)
		{
			playerIndex = currPlayers.size();
			currPlayers.add(new Player(name));
		}
		return playerIndex;
	}

	public int[] getDiceVal() // used by UI to load correct dice
	{
		int[] diceVal = new int[5];
		for (int i = 0; i < diceVal.length; i++)
		{
			diceVal[i] = dice[i].value;
		}
		return diceVal;
	}

	public boolean[] getKeepVal() // used by UI to track player keep/release
									// button
	{
		boolean[] keep = new boolean[5];
		for (int i = 0; i < keep.length; i++)
		{
			keep[i] = dice[i].keep;
		}
		return keep;
	}

	public String getPlayerName(int index)
	{
		return currPlayers.get(index).name;
	}

	public int getNumOfPlayers()
	{
		return currPlayers.size();
	}
	
	public int getPlayerScore(int index)
	{
		int total = currPlayers.get(index).total;
		return total;
	}

	public int updatePlayerScore(int index)
	{
		int newTotal = 0;
		for(int i = 0; i<currPlayers.get(index).score.length; i++)
		{
			newTotal += currPlayers.get(index).score[i];
		}
		return newTotal;
	}
	
	public int checkScore(int index)
	{
		int score = 0;
		score = currPlayers.get(0).score[index];
		System.out.println("index: " + index);
		System.out.println("check score: " + score);
		return score;
	}
	
	public int getRollCount()
	{
		return yu.getRollCounter();
	}

	public boolean isLegal(int index)
	{
		int score = 0;
		score = yu.checkScore(index, dice);
		if(score>0)
			return true;
		else
			return false;
	}
}

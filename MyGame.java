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
//end inner class dice

//start inner class player
class Player implements Serializable
{
	public String name;
	public int[] score;
	public int total;
	
	Player()
	{
		name = "NO NAME";
		score = new int[13];
		total = 0;
	}
	
	Player(String name)
	{
		this.name = name;
		score = new int[13];
		total = 0;
	}
}
//end inner class player


public class MyGame extends GameNet_CoreGame implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static final int MAX_PLAYERS = 4;
	private ArrayList<Player> currPlayers = new ArrayList<Player>();
	private Dice[] dice = new Dice[5];
	private YahtzeeUtil yu = new YahtzeeUtil();
	

	public Object process(Object inputs)
	{
		MyGameInput myGameInput = (MyGameInput) inputs;		
		int playerIndex = getPlayerIndex(myGameInput.myName);
		
		if(playerIndex < 0)
		{
			System.out.println("Already have max amount of players");
			return null;
		}
		
		switch(myGameInput.command)
		{
		case MyGameInput.JOIN_GAME:
			dice = yu.initializeDice(dice);
			break;
		case MyGameInput.ROLL_DICE:
			dice = yu.rollDice(playerIndex, dice);
			break;
		case MyGameInput.KEEP_DIE:
			dice = yu.setKeep(myGameInput.index, dice);
			break;
		case MyGameInput.RELEASE_DIE:
			dice = yu.setRelease(myGameInput.index, dice);
			break;
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
		
		for(int i = 0; i<currPlayers.size(); i++)
		{
			if(name.equals(currPlayers.get(i).name))
				return i;
		}

		if(currPlayers.size() < MAX_PLAYERS)
		{
			playerIndex = currPlayers.size();
			currPlayers.add(new Player(name));
		}
		return playerIndex;
	}
	
	public int[] getDiceVal()
	{
		int[] diceVal = new int[5];
		for(int i =0; i<diceVal.length; i++)
		{
			diceVal[i] = dice[i].value;
		}
		return diceVal;
	}
	

}


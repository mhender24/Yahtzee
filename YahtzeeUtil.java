package yahtzee;

import java.io.Serializable;
import java.util.Random;

public class YahtzeeUtil implements Serializable
{
	private static final long serialVersionUID = 1L;

	private static final boolean PASS = false;
	private static final boolean HOLD = true;
	private static final int GAME_IN_PROGRESS = -1;
	private static final int MAX_REROLL = 3;

	private int gameWinner = GAME_IN_PROGRESS;
	private int currTurn = 0;

	public Dice[] initializeDice(Dice[] dice)
	{
		Dice[] d = new Dice[5];
		for (int i = 0; i < d.length; i++)
		{
			d[i] = new Dice();
		}
		return d;
	}

	public Dice[] rollDice(int playerIndex, Dice[] dice)
	{
		Random diceRoll = new Random();
		Dice[] rolledDice = dice;
		int roll = 0;

		if (myTurn(playerIndex))
		{
			for (int i = 0; i < rolledDice.length; i++)
			{
				if (rolledDice[i].keep == false)
				{
					roll = diceRoll.nextInt(6) + 1;
					rolledDice[i].value = roll;
				}
			}
			return rolledDice;
		}
		else
			return null;
	}

	private boolean myTurn(int playerIndex)
	{
		if (playerIndex == currTurn)
			return true;
		else
			return false;
	}

	public Dice[] setKeep(int index, Dice[] dice)
	{
		Dice[] temp = dice;

			dice[index].keep = true;
			return temp;

	}
	
	public Dice[] setRelease(int index, Dice[] dice)
	{
		Dice[] temp = dice;

			dice[index].keep = false;
			return temp;

	}
}

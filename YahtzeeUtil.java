package yahtzee;

import java.io.Serializable;
import java.util.Random;

public class YahtzeeUtil implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static final int MAX_REROLL = 3;
    private static final int MAX_PLAYERS = 2;

    private int currTurn = 0;
    private int rollCounter = 0;

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

        if (myTurn(playerIndex) && rollCounter < MAX_REROLL)
        {
            for (int i = 0; i < rolledDice.length; i++)
            {
                if (rolledDice[i].keep == false)
                {
                    roll = diceRoll.nextInt(6) + 1;
                    rolledDice[i].value = roll;
                }
            }
            rollCounter += 1;
            return rolledDice;
        }
        else if (!myTurn(playerIndex))
            return dice;
        else
        {
            return dice;
        }
    }
    
    public int getCurrPlayerIndex()
    {
        return currTurn;
    }
    
    public int getRollCounter()
    {
        return rollCounter;
    }
    
    public int calcPlayerTotal(Player player)
    {
        int total = 0;
        for (int i = 0; i < player.score.length; i++)
        {
            total += player.score[i];
        }
        return total;
    }
    
    public void incremenetCurrTurn()
    {
        currTurn+=1;
        if(currTurn==MAX_PLAYERS)
            currTurn = 0;
    }
    
    public void resetRollCounter()
    {
        rollCounter = 0;
    }
    
    private boolean myTurn(int playerIndex)
    {
        if (playerIndex == currTurn)
            return true;
        else
            return false;
    }

    public Dice[] setKeep(int playerIndex, int diceIndex, Dice[] dice)
    {
        if (myTurn(playerIndex))
        {
            Dice[] temp = dice;

            dice[diceIndex].keep = true;
            return temp;
        }
        return dice;
    }

    public Dice[] setRelease(int playerIndex, int diceIndex, Dice[] dice)
    {
        if (myTurn(playerIndex))
        {
            Dice[] temp = dice;

            dice[diceIndex].keep = false;
            return temp;
        }
        return dice;
    }

    public void gameOver()
    {
        currTurn = -1;
    }
    
    public int checkScore(int index, Dice[] dice)
    {
        int score = 0;
        
        switch (index)
        {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            score = checkUpperScore(index, dice);
            break;
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
            score = checkLowerScore(index, dice);
            break;
        }
        rollCounter = 0;
        
        return score;
    }
    
    private int checkUpperScore(int index, Dice[] dice)     //
    {
        int score = 0;
        
        System.out.println(index);
        
        for(int i = 0; i<dice.length; i++)
        {
            if(dice[i].value == (index+1))
                score += (index+1);
        }
        if(score >= 0)
        {
            return score;
        }
        else
            return -1;            //checks for error
    }
    
    private int checkLowerScore(int index, Dice[] dice)
    {
        int one=0, two=0, three=0, four=0, five=0, six =0;      //checks the num of die with similar values
        for(int i = 0; i<dice.length; i++)
        {
            switch(dice[i].value)
            {
            case 1: 
                one+=1;
                break;
            case 2:
                two+=1;
                break;
            case 3: 
                three+=1;
                break;
            case 4:
                four+=1;
                break;
            case 5:
                five+=1;
                break;
            case 6:
                six+=1;
                break;
            }
        }
        if(index == 6)    //checks for a 3 of a kind
        {
            if(one>=3)
                return 3;
            else if(two>=3)
                return 6;
            else if(three>=3)
                return 9;
            else if(four>=3)
                return 12;
            else if(five>=3)
                return 15;
            else if(six>=3)
                return 18;
        }
        else if(index == 7)    //checks for a 4 of a kind
        {
            if(one>=4)
                return 4;
            else if(two>=4)
                return 8;
            else if(three>=4)
                return 12;
            else if(four>=4)
                return 16;
            else if(five>=4)
                return 20;
            else if(six>=4)
                return 24;
        }
        else if(index == 8)    //checks for a full house
        {
            if((one == 2 || two == 2 || three == 2 || four == 2 || five == 2 || six == 2) && 
                    (one == 3 || two == 3 || three == 3 || four == 3 || five == 3 || six == 3))
                return 25;
        }
        else if(index == 9)    //checks for a sm. straight
        {
            if((one >= 1 && two >= 1 && three >= 1 && four >= 1) ||
                    (two >=1 && three >=1 && four >=1 && five >=1) ||
                    (three>=1 && four >=1 && five >=1 && six >=1))
                return 30;
        }
        else if(index == 10)    //checks for a lg. straight
        {
            if((one == 1 && two == 1 && three == 1 && four == 1 && five == 1) ||
                    (two ==1 && three ==1 && four ==1 && five ==1 && six == 1))
                return 40;
        }
        else if(index == 11)  //checks for yahtzee
        {
            if(one==5 || two == 5 || three == 5 || four == 5 || five == 5 || six == 5)
                return 50;
        }
        else if(index == 12)   //add up chance 
        {
            int score = 0;
            for(int i = 0; i<dice.length; i++)
            {
                score += dice[i].value;
            }
            return score;
        }
        else
            return -1;
        return -1;
    }

}
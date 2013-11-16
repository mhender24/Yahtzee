package yahtzee;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class MyUserInterface extends JFrame implements ActionListener,
        GameNet_UserInterface, Serializable
{
    private static final long serialVersionUID = 1L;
    private static final int DICE_SIZE = 80;
    private static final int NUM_OF_DICE = 5;
    private static final int MAX_PLAYERS = 2;
    private int[] diceVal;
    private CardLayout cardLayoutMgr = new CardLayout();
    private JPanel scorePanel = new JPanel();
    private JPanel gameDisplay = new JPanel();
    private JPanel scoreDisplay = new JPanel();
    private JLabel playerScoreArr[] = new JLabel[MAX_PLAYERS];
    private JButton[] keepArr = new JButton[5];
    private JButton[] releaseArr = new JButton[5];
    private JButton keepAll = new JButton("Keep All");
    private JButton rollDice = new JButton("Roll Dice");
    private JRadioButton[] radioSelection = new JRadioButton[13];
    private GamePlayer myGamePlayer = null;
    private MyGame myGame = null;
    private String myName = "";
    private MyGameInput myGameInput = new MyGameInput();
    Image[] diceImage;

    private Image[] loadDice()
    {
        Image[] temp = new Image[6];
        temp[0] = new ImageIcon("Dice1.jpg").getImage();
        temp[1] = new ImageIcon("Dice2.jpg").getImage();
        temp[2] = new ImageIcon("Dice3.jpg").getImage();
        temp[3] = new ImageIcon("Dice4.jpg").getImage();
        temp[4] = new ImageIcon("Dice5.jpg").getImage();
        temp[5] = new ImageIcon("Dice6.jpg").getImage();
        return temp;
    }

    private void displayDice(int[] diceVal, Graphics g)
    {
        int height = 800;

        for (int i = 0; i < diceVal.length; i++)
        {
            switch (diceVal[i])
            {
            case 1:
                g.drawImage(diceImage[0], 10, height - 715, DICE_SIZE,
                        DICE_SIZE, null);
                break;
            case 2:
                g.drawImage(diceImage[1], 10, height - 715, DICE_SIZE,
                        DICE_SIZE, null);
                break;
            case 3:
                g.drawImage(diceImage[2], 10, height - 715, DICE_SIZE,
                        DICE_SIZE, null);
                break;
            case 4:
                g.drawImage(diceImage[3], 10, height - 715, DICE_SIZE,
                        DICE_SIZE, null);
                break;
            case 5:
                g.drawImage(diceImage[4], 10, height - 715, DICE_SIZE,
                        DICE_SIZE, null);
                break;
            case 6:
                g.drawImage(diceImage[5], 10, height - 715, DICE_SIZE,
                        DICE_SIZE, null);
                break;
            default:
                break;
            }
            height += 140;
        }
    }

    private void sendMessage(int command)
    {
        myGameInput.command = command;
        myGamePlayer.sendMessage(myGameInput);
    }

    private void sendKeepMessage(int index)
    {
        myGameInput.index = index;
        sendMessage(MyGameInput.KEEP_DIE);
    }

    private void sendReleaseMessage(int index)
    {
        myGameInput.index = index;
        sendMessage(MyGameInput.RELEASE_DIE);
    }
    private void sendCheckScoreMessage(int index)
    {
        myGameInput.index = index;
        sendMessage(MyGameInput.CHECK_SCORE);
    }
    private void sendPassScoreMessage()
    {
        sendMessage(MyGameInput.PASS_SCORE);
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();
        boolean flag = false;
        int rollCounter =0;

        for (int i = 0; i < keepArr.length; i++)
        {
            if (e.getSource() == keepArr[i])
                sendKeepMessage(i);
        }

        for (int i = 0; i < keepArr.length; i++)
        {
            if (e.getSource() == releaseArr[i])
                sendReleaseMessage(i);
        }

        if (action.equals("Keep All"))
            cardLayoutMgr.show(this.getContentPane(), "ScoreDisplay");
        
        else if (action.equals("Roll Dice"))
        {
            rollCounter = myGame.getRollCount();

            if(rollCounter==2)    //allows for 3 rolls before switching to scoreDisplay
                cardLayoutMgr.show(this.getContentPane(), "ScoreDisplay");
            sendMessage(MyGameInput.ROLL_DICE);
        }
        else if (action.equals("Submit"))
        {
            for(int i = 0; i< radioSelection.length; i++)
            {
                if(radioSelection[i].isSelected())
                {
                    flag = myGame.isLegal(i);
                    if(flag)
                    {
                        cardLayoutMgr.show(this.getContentPane(), "GameDisplay");
                        sendCheckScoreMessage(i);
                    }

                }
            }
        }
        else if(action.equals("Pass"))
        {
            sendPassScoreMessage();
            cardLayoutMgr.show(this.getContentPane(), "GameDisplay");

        }
    }

    @Override
    public void receivedMessage(Object ob)
    {
        MyGameOutput myGameOutput = (MyGameOutput) ob;
        myGame = myGameOutput.myGame;
        diceVal = myGame.getDiceVal();
        boolean[] keepFlag = myGame.getKeepVal();
        int numOfPlayers = myGame.getNumOfPlayers();
        // String msg = myGame.getStatus(myName);
        // String turnMsg = myGame.getTurnInfo(myName);
        // String extendedName = myGame.getExtendedName(myName);
        for (int i = 0; i < keepFlag.length; i++)
        {
            if (keepFlag[i] == true)
            {
                keepArr[i].setVisible(false);
                releaseArr[i].setVisible(true);
            }
            else
            {
                keepArr[i].setVisible(true);
                releaseArr[i].setVisible(false);
            }
        }

        for (int i = 0; i < numOfPlayers; i++)
        {
            String playerName = myGame.getPlayerName(i) + ": "
                    + myGame.getPlayerScore(i);
            if (playerScoreArr[i] != null)
                playerScoreArr[i].setText(playerName);
        }
        repaint();
    }

    @Override
    public void startUserInterface(GamePlayer gamePlayer)
    {
        myGamePlayer = gamePlayer;
        myName = gamePlayer.getPlayerName();
        myGameInput.setName(myName);

        for (int i = 0; i < keepArr.length; i++)
        {
            keepArr[i] = new JButton("Keep");
            releaseArr[i] = new JButton("Release");
        }

        sendMessage(MyGameInput.JOIN_GAME);

        // addWindowListener(this.closeMonitor);
        display();
        setVisible(true);
    }

    private void gameDisplay()
    {
        
    }
    
    private void scoreDisplay()
    {
        
    }
    
    
    private void display()
    {
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setLayout(cardLayoutMgr);
        
        gameDisplay.setLayout(new BorderLayout());
        scoreDisplay.setLayout(new BorderLayout());
        
        scorePanel.setLayout(new GridLayout(5, 1));
        scorePanel.setBackground(Color.red);

        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayout(1, 2));
        optionPanel.setBackground(Color.green);
        rollDice.addActionListener(this);
        optionPanel.add(rollDice);
        keepAll.addActionListener(this);
        optionPanel.add(keepAll);

        JLabel scoreLabel = new JLabel("Current Score");
        scoreLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
        scorePanel.add(scoreLabel);

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(5, 3, 0, 0));
        gamePanel.setBackground(Color.blue);

        JPanel chatPanel = new JPanel();
        chatPanel.setBackground(Color.gray);

        JTextField typeArea = new JTextField(60);
        typeArea.setBackground(Color.green);
        chatPanel.add(typeArea);

        JMenu exitPanel = new JMenu("File");

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        exitPanel.add(exit);

        JMenuBar bar = new JMenuBar();
        bar.setBackground(Color.gray);
        bar.add(exitPanel);
        setJMenuBar(bar);

        for (int i = 0; i < MAX_PLAYERS; i++)
        {
            playerScoreArr[i] = new JLabel();
            playerScoreArr[i].setFont(new Font(Font.SERIF, Font.PLAIN, 15));
            scorePanel.add(playerScoreArr[i]);
        }
        sendMessage(MyGameInput.NO_COMMAND); // updates score labels

        gameDisplay.add(scorePanel, BorderLayout.EAST);
        gameDisplay.add(gamePanel, BorderLayout.CENTER);
        gameDisplay.add(optionPanel, BorderLayout.SOUTH);

        JLabel[] blankLabel2 = new JLabel[5];              //provides space for dice

        for (int i = 0; i < 5; i++) // test
        {
            blankLabel2[i]= new JLabel("                 ");
            gamePanel.add(blankLabel2[i]);
            keepArr[i].setBackground(Color.blue);
            keepArr[i].addActionListener(this);
            gamePanel.add(keepArr[i]);
            releaseArr[i].addActionListener(this);
            releaseArr[i].setVisible(false);
            gamePanel.add(releaseArr[i]);
        }

        String[] strScoreTitle = {"Aces", "Twos", "Threes", "Fours", "Fives", "Sixes", "3 of a Kind", "4 of a Kind",
                "Full House", "Sm. Straight", "Lg. Straight", "Yahtzee", "Chance"};
        JLabel[] labelScoreTitle = new JLabel[13];            //provides space for dice
        JLabel[] blankLabel = new JLabel[13]; 
        ButtonGroup scoreList = new ButtonGroup();
        radioSelection = new JRadioButton[13];
        JButton submit = new JButton("Submit");
        JButton pass = new JButton("Pass");
        JPanel scoreCard = new JPanel();
        scoreCard.setLayout(new GridLayout(14,3,0,0));
        scoreDisplay.setLayout(new BorderLayout());
        for(int i =0; i<labelScoreTitle.length; i++)
        {
            blankLabel[i] = new JLabel("                     ");      
            labelScoreTitle[i] = new JLabel(strScoreTitle[i]);
            radioSelection[i] = new JRadioButton(strScoreTitle[i], false);
            radioSelection[i].addActionListener(this);
            labelScoreTitle[i].setFont(new Font(Font.SERIF, Font.PLAIN, 40));
            scoreList.add(radioSelection[i]);
            scoreCard.add(blankLabel[i]);
            scoreCard.add(labelScoreTitle[i]);
            scoreCard.add(radioSelection[i]);
        }
        
        submit.addActionListener(this);
        pass.addActionListener(this);
        scoreCard.add(submit);
        scoreCard.add(pass);
        scoreDisplay.add(scoreCard, BorderLayout.CENTER);
        
        add("GameDisplay", gameDisplay);
        add("ScoreDisplay", scoreDisplay);
        
        cardLayoutMgr.show(this.getContentPane(), "GameDisplay");
        
        setVisible(true);
    }

    public void paint(Graphics g)
    {
        super.paint(g);
        diceImage = loadDice();

        displayDice(diceVal, g);
    }

}
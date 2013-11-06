package yahtzee;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import java.awt.BorderLayout;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyUserInterface extends JFrame implements ActionListener,
		GameNet_UserInterface, Serializable
{
	private static final long serialVersionUID = 1L;
	private static final int DICE_SIZE = 80;
	private static final int NUM_OF_DICE = 5;
	private int[] diceVal;
	private JPanel gamePanel;
	private JButton[] keepArr = new JButton[5];
	private JButton[] releaseArr = new JButton[5];
	private JButton keepAll = new JButton("Keep All");
	private JButton rollDice = new JButton("Roll Dice");
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

	private void loadKeepArr()
	{

	}

	public static void main(String[] Args)
	{
		MyUserInterface ui = new MyUserInterface();
		ui.display();
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

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String action = e.getActionCommand();

		for (int i = 0; i < keepArr.length; i++)
		{
			if (e.getSource() == keepArr[i])
			{
				keepArr[i].setVisible(false);
				releaseArr[i].setVisible(true);
				sendKeepMessage(i);
			}
		}

		for (int i = 0; i < keepArr.length; i++)
		{
			if (e.getSource() == releaseArr[i])
			{
				keepArr[i].setVisible(true);
				releaseArr[i].setVisible(false);
				sendReleaseMessage(i);
			}
		}

		if (action.equals("Keep All"))
		{

		}
		else if (action.equals("Roll Dice"))
		{
			sendMessage(MyGameInput.ROLL_DICE);
		}
	}

	@Override
	public void receivedMessage(Object ob)
	{
		MyGameOutput myGameOutput = (MyGameOutput) ob;
		myGame = myGameOutput.myGame;

		// String msg = myGame.getStatus(myName);
		// String turnMsg = myGame.getTurnInfo(myName);
		// String extendedName = myGame.getExtendedName(myName);

		diceVal = myGame.getDiceVal();
		
		repaint();

	}

	@Override
	public void startUserInterface(GamePlayer gamePlayer)
	{
		myGamePlayer = gamePlayer;
		myName = gamePlayer.getPlayerName();
		myGameInput.setName(myName);

		sendMessage(MyGameInput.JOIN_GAME);

		// addWindowListener(this.closeMonitor);
		display();
		setVisible(true);
	}

	private void display()
	{
		setLayout(new BorderLayout());

		setSize(1200, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel scorePanel = new JPanel();
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

		JLabel playerScoreArr[] = new JLabel[4];
		for (int i = 0; i < playerScoreArr.length; i++)
		{
			String playerName = "Player " + i + "    "; // need to change output
			playerScoreArr[i] = new JLabel(playerName);
			playerScoreArr[i].setFont(new Font(Font.SERIF, Font.PLAIN, 15));
			scorePanel.add(playerScoreArr[i]);
		}

		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(5, 1, 0, 0));
		gamePanel.setBackground(Color.blue);

		// JPanel dicePanel = new JPanel();
		// dicePanel.setLayout(new GridLayout(5,1,0,0));
		// dicePanel.setBackground(Color.pink);

		JPanel chatPanel = new JPanel();
		chatPanel.setBackground(Color.gray);

		JTextField typeArea = new JTextField(60);
		typeArea.setBackground(Color.green);
		chatPanel.add(typeArea);

		JTextArea chat = new JTextArea(8, 60);
		chat.setBackground(Color.yellow);
		chatPanel.add(chat);

		JMenu exitPanel = new JMenu("File");

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		exitPanel.add(exit);

		JMenuBar bar = new JMenuBar();
		bar.setBackground(Color.gray);
		bar.add(exitPanel);
		setJMenuBar(bar);

		add(scorePanel, BorderLayout.EAST);
		add(gamePanel, BorderLayout.CENTER);
		add(optionPanel, BorderLayout.SOUTH);
		// add(dicePanel, BorderLayout.WEST);

		for (int i = 0; i < 5; i++) // test
		{
			keepArr[i] = new JButton("Keep");
			keepArr[i].setBackground(Color.blue);
			keepArr[i].addActionListener(this);
			gamePanel.add(keepArr[i]);
			releaseArr[i] = new JButton("Release");
			releaseArr[i].addActionListener(this);
			releaseArr[i].setVisible(false);
			gamePanel.add(releaseArr[i]);
		}

		setVisible(true);
	}

	public void paint(Graphics g)
	{
		super.paint(g);

		Dimension d = getSize();
		int width = d.width;
		int height = d.height;

		Insets inset = getInsets();
		int top = inset.top;
		int bottom = inset.bottom;
		int left = inset.left;
		int right = inset.right;

		// g.drawRect(width-229, height-300, 5, 5);
		int diceSpacing = (((width - 234) / NUM_OF_DICE) - DICE_SIZE) / 2;

		diceImage = loadDice();

		displayDice(diceVal, g);
		// loadDice(dice);
		/*
		 * g.drawRect(diceSpacing, height-500, DICE_SIZE, DICE_SIZE);
		 * g.drawRect((diceSpacing*3)+(DICE_SIZE*1), height-500, DICE_SIZE,
		 * DICE_SIZE); g.drawRect((diceSpacing*5)+(DICE_SIZE*2), height-500,
		 * DICE_SIZE, DICE_SIZE); g.drawRect((diceSpacing*7)+(DICE_SIZE*3),
		 * height-500, DICE_SIZE, DICE_SIZE);
		 * g.drawRect((diceSpacing*9)+(DICE_SIZE*4), height-500, DICE_SIZE,
		 * DICE_SIZE);
		 * 
		 * g.fillOval(diceSpacing+30, height-470, 20, 20);
		 * g.drawRect((diceSpacing*3)+(DICE_SIZE*1), height-500, DICE_SIZE,
		 * DICE_SIZE); g.drawRect((diceSpacing*5)+(DICE_SIZE*2), height-500,
		 * DICE_SIZE, DICE_SIZE); g.drawRect((diceSpacing*7)+(DICE_SIZE*3),
		 * height-500, DICE_SIZE, DICE_SIZE);
		 * g.drawRect((diceSpacing*9)+(DICE_SIZE*4), height-500, DICE_SIZE,
		 * DICE_SIZE);
		 */
		repaint();
	}

}

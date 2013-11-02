package yahtzee;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MyUserInterface extends JFrame implements ActionListener, GameNet_UserInterface
{
	private MyGame myGame = null;
	private GamePlayer myGamePlayer = null;
	private String myName = "";
	private MyGameInput myGameInput = new MyGameInput();
	
	private JButton[] keepArr = new JButton[5];
	private JButton keepAllButton = new JButton("Keep All");
	private JButton rollDice = new JButton("Roll Dice");
	

	
	private void display()
	{
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new FlowLayout());
		
		JLabel player1 = new JLabel("player1: 20");
		scorePanel.add(player1);
		
		JLabel player2 = new JLabel("player1: 20");
		scorePanel.add(player2);
		
		JLabel player3 = new JLabel("player1: 20");
		scorePanel.add(player3);
		
		JLabel player4 = new JLabel("player1: 20");
		scorePanel.add(player4);
		
		JPanel dicePanel = new JPanel();
		
		JPanel chatPanel = new JPanel();

		
		JMenu exitPanel = new JMenu("Exit");

		JMenuItem exit = new JMenuItem();
		exit.addActionListener(this);
		exitPanel.add(exit);
		
		JMenuBar bar = new JMenuBar();
		bar.add(exitPanel);
		setJMenuBar(bar);
		
		add(bar, BorderLayout.NORTH);
		add(scorePanel, BorderLayout.EAST);
		add(dicePanel, BorderLayout.CENTER);
		add(chatPanel, BorderLayout.SOUTH);
		
		


		
		setLayout(new BorderLayout());
		

	}
	
	public void paint(Graphics g)
	{
		
	}
	
	public void receivedMessage(Object ob)
	{
		
	}

	public void startUserInterface(GamePlayer player)
	{
		
	}

	public void actionPerformed(ActionEvent arg0)
	{
		
	}

}

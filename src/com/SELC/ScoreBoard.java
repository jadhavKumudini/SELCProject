package com.SELC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ScoreBoard extends JPanel {
	
	private static final long serialVersionUID = -40557434900946408L;

	private static final Font SMALL_FONT = new Font("Times New Roman", Font.BOLD, 12);
	
	private SnakeMazeGame game;
	
	public ScoreBoard(SnakeMazeGame game) {
		this.game = game;
		
		setPreferredSize(new Dimension(10, 60));
		setBackground(Color.BLUE);
	}
	
	private static final int S_OFFSET = 10;
	
	private static final int MESSAGE_STR = 10;
	
	private static final int L_OFFSET = 10;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.ORANGE);
		
		g.setFont(SMALL_FONT);
		
		int drawY = S_OFFSET;
		g.drawString("Total Score: " + game.getScore(), L_OFFSET, drawY += MESSAGE_STR);
		
	}

}

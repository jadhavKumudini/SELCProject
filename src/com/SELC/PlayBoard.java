package com.SELC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

public class PlayBoard extends JPanel {
	
	private static final long serialVersionUID = -1102632585936751117L;
	public static final int C_COUNT = 25;	
	public static final int R_COUNT = 25;	
	public static final int CELL_SIZE = 20;
	private static final int EYE_LINSET = CELL_SIZE / 3;	
	private static final int EYE_SINSET = CELL_SIZE / 6;
	private static final int EYE_LENGTH = CELL_SIZE / 5;
	private static final Font FONT = new Font("Times New Roman", Font.BOLD, 25);
	private SnakeMazeGame game;
	private CellType[] tiles;
	
	public PlayBoard(SnakeMazeGame game) {
		this.game = game;
		this.tiles = new CellType[R_COUNT * C_COUNT];
		
		setPreferredSize(new Dimension(C_COUNT * CELL_SIZE, R_COUNT * CELL_SIZE));
		setBackground(Color.WHITE);
	}
	
	public void makeCleanBoard() {
		for(int i = 0; i < tiles.length; i++) {
			tiles[i] = null;
		}
	}
	
	public void setCell(Point point, CellType type) {
		setCell(point.x, point.y, type);
	}
	
	public void setCell(int x, int y, CellType type) {
		tiles[y * R_COUNT + x] = type;
	}

	public CellType getCell(int x, int y) {
		return tiles[y * R_COUNT + x];
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for(int x = 0; x < C_COUNT; x++) {
			for(int y = 0; y < R_COUNT; y++) {
				CellType type = getCell(x, y);
				if(type != null) {
					drawCell(x * CELL_SIZE, y * CELL_SIZE, type, g);
				}
			}
		}
		
		g.setColor(Color.DARK_GRAY);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		for(int x = 0; x < C_COUNT; x++) {
			for(int y = 0; y < R_COUNT; y++) {
				g.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, getHeight());
				g.drawLine(0, y * CELL_SIZE, getWidth(), y * CELL_SIZE);
			}
		}		
	
		if(game.isGameEnd() || game.isNewStartGame() || game.isPaused()) {
			g.setColor(Color.MAGENTA);
			
			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;
			
			String lMessage = null;
			String sMessage = null;
			if(game.isNewStartGame()) {
			lMessage = "Snake Maze Game!";
		    sMessage = "Press Enter to Start";
			} else if(game.isGameEnd()) {
		    lMessage = "Game End!";
			sMessage = "Press Enter to Restart";
			} else if(game.isPaused()) {
			lMessage = "Paused";
			sMessage = "Press P to Resume";
			}
			
		
			g.setFont(FONT);
			g.drawString(lMessage, centerX - g.getFontMetrics().stringWidth(lMessage) / 2, centerY - 50);
			g.drawString(sMessage, centerX - g.getFontMetrics().stringWidth(sMessage) / 2, centerY + 50);
		}
	}
	
	private void drawCell(int x, int y, CellType type, Graphics g) {
	
		switch(type) {
		
		
		case Food:
			g.setColor(Color.RED);
			g.fillOval(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
			break;
		
		case SBody:
			g.setColor(Color.GREEN);
			g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
			break;
		
		case Maze:
			g.setColor(Color.darkGray);
			g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
			break;
			
		
		case SHead:
			g.setColor(Color.ORANGE);
			g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
			
			g.setColor(Color.BLACK);
			
			
			switch(game.getDirection()) {
			case NorthDirection: {
				int baseY = y + EYE_SINSET;
				g.drawLine(x + EYE_LINSET, baseY, x + EYE_LINSET, baseY + EYE_LENGTH);
				g.drawLine(x + CELL_SIZE - EYE_LINSET, baseY, x + CELL_SIZE - EYE_LINSET, baseY + EYE_LENGTH);
				break;
			}
				
			case SouthDirection: {
				int baseY = y + CELL_SIZE - EYE_SINSET;
				g.drawLine(x + EYE_LINSET, baseY, x + EYE_LINSET, baseY - EYE_LENGTH);
			
				g.drawLine(x + CELL_SIZE - EYE_LINSET, baseY, x + CELL_SIZE - EYE_LINSET, baseY - EYE_LENGTH);
				break;
			}
			
			case WestDirection: {
				int baseX = x + EYE_SINSET;
				g.drawLine(baseX, y + EYE_LINSET, baseX + EYE_LENGTH, y + EYE_LINSET);
				g.drawLine(baseX, y + CELL_SIZE - EYE_LINSET, baseX + EYE_LENGTH, y + CELL_SIZE - EYE_LINSET);
				break;
			}
				
			case EastDirection: {
				int baseX = x + CELL_SIZE - EYE_SINSET;
				g.drawLine(baseX, y + EYE_LINSET, baseX - EYE_LENGTH, y + EYE_LINSET);
				g.drawLine(baseX, y + CELL_SIZE - EYE_LINSET, baseX - EYE_LENGTH, y + CELL_SIZE - EYE_LINSET);
				break;
			}
			
			}
			break;
		}
	}

}

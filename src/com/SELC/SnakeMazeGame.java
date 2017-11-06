package com.SELC;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;

public class SnakeMazeGame extends JFrame {
		
	private static final long serialVersionUID = 6678292058307427777L;

	private static final long GAME_TIME = 2000L / 50L;
	
	private static final int MIN_LEN = 4;
	
	private static final int MAX_DIRECTIONS = 3;
	
    private boolean isNewStartGame;
	
	private boolean isGameEnd;
	
	private boolean isPaused;
	
	private PlayBoard board;
	
    private ScoreBoard scSide;
	
	private Random random;
	
	private Timer logicGameTimer;
	
	private int score;
	
	private int foodEaten;
	
	private int nextFoodScore;
	
    private LinkedList<Point> snakePoint;
	
	private LinkedList<Direction> directionsMovement;
	
	
	private SnakeMazeGame() {
		super("Snake Maze");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		this.board = new PlayBoard(this);
		this.scSide = new ScoreBoard(this);
		
		add(board, BorderLayout.CENTER);
		add(scSide, BorderLayout.SOUTH);
		
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {

				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
					if(!isPaused && !isGameEnd) {
						if(directionsMovement.size() < MAX_DIRECTIONS) {
							Direction last = directionsMovement.peekLast();
							if(last != Direction.SouthDirection && last != Direction.NorthDirection) {
								directionsMovement.addLast(Direction.NorthDirection);
							}
						}
					}
					break;

			
				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
					if(!isPaused && !isGameEnd) {
						if(directionsMovement.size() < MAX_DIRECTIONS) {
							Direction last = directionsMovement.peekLast();
							if(last != Direction.NorthDirection && last != Direction.SouthDirection) {
								directionsMovement.addLast(Direction.SouthDirection);
							}
						}
					}
					break;
								
				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
					if(!isPaused && !isGameEnd) {
						if(directionsMovement.size() < MAX_DIRECTIONS) {
							Direction last = directionsMovement.peekLast();
							if(last != Direction.EastDirection && last != Direction.WestDirection) {
								directionsMovement.addLast(Direction.WestDirection);
							}
						}
					}
					break;

				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
					if(!isPaused && !isGameEnd) {
						if(directionsMovement.size() < MAX_DIRECTIONS) {
							Direction last = directionsMovement.peekLast();
							if(last != Direction.WestDirection && last != Direction.EastDirection) {
								directionsMovement.addLast(Direction.EastDirection);
							}
						}
					}
					break;
	
				case KeyEvent.VK_P:
					if(!isGameEnd) {
						isPaused = !isPaused;
						logicGameTimer.setPaused(isPaused);
					}
					break;

				case KeyEvent.VK_ENTER:
					if(isNewStartGame || isGameEnd) {
						resetGame();
					}
					break;
				}
			}
			
		});
		

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	
	private CellType updateSnakeMovement() {

		Direction direction = directionsMovement.peekFirst();
			
		Point head = new Point(snakePoint.peekFirst());
		switch(direction) {
		case NorthDirection:
			head.y--;
			break;
			
		case SouthDirection:
			head.y++;
			break;
			
		case WestDirection:
			head.x--;
			break;
			
		case EastDirection:
			head.x++;
			break;
		}
		
		
	if(head.x < 0 || head.x >= PlayBoard.C_COUNT || head.y < 0 || head.y >= PlayBoard.R_COUNT ) {
			return CellType.SBody; 
		}
		
		
		CellType old = board.getCell(head.x, head.y);
		if(old != CellType.Food && snakePoint.size() > MIN_LEN) {
			Point tail = snakePoint.removeLast();
			board.setCell(tail, null);
			old = board.getCell(head.x, head.y);
		}
		
		
		if(old != CellType.SBody) {
			board.setCell(snakePoint.peekFirst(), CellType.SBody);
			snakePoint.push(head);
			board.setCell(head, CellType.SHead);
			if(directionsMovement.size() > 1) {
				directionsMovement.poll();
			}
		}
				
		return old;
	}
	
	private void resetGame() {
		
     	this.score = 0;
		this.foodEaten = 0;
		
		
		this.isNewStartGame = false;
		this.isGameEnd = false;
		
		Point head = new Point(PlayBoard.C_COUNT / 2, PlayBoard.R_COUNT / 2);

		snakePoint.clear();
		snakePoint.add(head);
		
		board.makeCleanBoard();
		board.setCell(head, CellType.SHead);
		
		directionsMovement.clear();
		directionsMovement.add(Direction.NorthDirection);
		
		logicGameTimer.reset();
		
		spawnFood();
		
		
		int j =0;
		while(j<10){
			j++;
		spawnMaze();
		}
	}
	
	public boolean isNewStartGame() {
		return isNewStartGame;
	}
	
	public boolean isGameEnd() {
		return isGameEnd;
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	private void spawnFood() {
		this.nextFoodScore = 100;

		
		int index = random.nextInt(PlayBoard.C_COUNT * PlayBoard.R_COUNT - snakePoint.size());
		
		
		int freeFound = -1;
		for(int x = 0; x < PlayBoard.C_COUNT; x++) {
			for(int y = 0; y < PlayBoard.R_COUNT; y++) {
				CellType type = board.getCell(x, y);
				if(type == null || type == CellType.Food) {
					if(++freeFound == index) {
						board.setCell(x, y, CellType.Food);
						break;
					}
				}
			}
		}
	}
	
	public void spawnMaze(){
		int freeFound = -1;
		int index = random.nextInt(PlayBoard.C_COUNT * PlayBoard.R_COUNT);
		for(int x = 0; x < PlayBoard.C_COUNT; x++) {
			for(int y = 0; y < PlayBoard.R_COUNT; y++) {
				CellType type = board.getCell(x, y);
				if(type == null || type == CellType.Maze) {
					if(++freeFound == index) {
						board.setCell(x, y, CellType.Maze);
						break;
					}
				}
			}
		}
		
		
		
	}
	
	public int getScore() {
		
		return score;
	}
	
	public int getFoodEaten() {
		return foodEaten;
	}
	
	public int getNextFoodScore() {

		return nextFoodScore;
	}
	
	
    private void updateSnakeMazeGame() {

		
		CellType clash = updateSnakeMovement();
		
		if(clash == CellType.Food) {
			foodEaten++;
			score += nextFoodScore;
			spawnFood();
			int i= 0;
			while(i<5){
				i++;
				spawnMaze();
			}
		} else if(clash == CellType.SBody) {
			isGameEnd = true;
			logicGameTimer.setPaused(true);
		} else if(clash == CellType.Maze ) {
			isGameEnd = true;
			logicGameTimer.setPaused(true);
		} 
		else if(nextFoodScore > 10) {
		  //nextFruitScore--;
		}
	}
	
    public Direction getDirection() {
		return directionsMovement.peek();
	}
	
    private void startSnakeMazeGame() {
		
		this.random = new Random();
		this.snakePoint = new LinkedList<>();
		this.directionsMovement = new LinkedList<>();
		this.logicGameTimer = new Timer(9.2f);
		this.isNewStartGame = true;
		
		logicGameTimer.setPaused(true);

		while(true) {
			long start = System.nanoTime();
			
	     	logicGameTimer.update();
	
			if(logicGameTimer.hasSlipCycle()) {
				updateSnakeMazeGame();
			}
			
			board.repaint();
			scSide.repaint();
		
			long delta = (System.nanoTime() - start) / 1200000L;
			if(delta < GAME_TIME) {
				try {
					Thread.sleep(GAME_TIME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		SnakeMazeGame snakeM = new SnakeMazeGame();
		snakeM.startSnakeMazeGame();
	}
}

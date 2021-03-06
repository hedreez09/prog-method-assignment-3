/*
  * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	private int brickCounter = 100;
	private GRect paddle;
	private double vx, vy;
	private GOval ball;
	private static final int DELAY = 17;
	private RandomGenerator rgen = RandomGenerator.getInstance();
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		for (int i = 0; i < NTURNS; i++){
		buildGame();
		playGame();
		if (brickCounter == 0){
			ball.setVisible(false);
			printWinner();
			break;
		}
		if(brickCounter > 0){
			removeAll();
		}
		}
		if(brickCounter > 0){
			printGameOver();
		}
		}
		private void buildGame(){
			setSize(WIDTH,HEIGHT);
			createBrick(0,BRICK_Y_OFFSET);
			createBall();
		  	createPaddle();
		  	
		  	
		}
		private void createBrick(double brx, double bry){
			for(int row = 0;row < NBRICK_ROWS; row++){
			for(int column = 0; column < NBRICKS_PER_ROW; column++){
				double y = bry +(BRICK_HEIGHT + BRICK_SEP)*row;
				double x = brx +(BRICK_WIDTH + BRICK_SEP)*column;
				GRect brick = new GRect(x,y,BRICK_WIDTH,BRICK_HEIGHT);
				
				
				switch (row){
				case 0:brick.setColor(Color.RED);break;
				case 1:brick.setColor(Color.RED);break;
				case 2:brick.setColor(Color.ORANGE);break;
				case 3:brick.setColor(Color.ORANGE);break;
				case 4:brick.setColor(Color.YELLOW);break;
				case 5:brick.setColor(Color.YELLOW);break;
				case 6:brick.setColor(Color.GREEN);break;
				case 7:brick.setColor(Color.GREEN);break;
				case 8:brick.setColor(Color.CYAN);break;
				case 9:brick.setColor(Color.CYAN);break;
				default:;break;
				}
				brick.setFilled(true);
				add(brick);
		}	
		}
			}
		
		/* You fill this in, along with any subsidiary methods */

	private void createBall(){
		ball = new GOval(WIDTH/2-BALL_RADIUS,HEIGHT/2-BALL_RADIUS,BALL_RADIUS,BALL_RADIUS);
		ball.setColor(Color.BLACK);
		ball.setFilled(true);
		add(ball);
	}
	private void createPaddle(){
		paddle = new GRect((getWidth()-PADDLE_WIDTH)/2,(getHeight()-PADDLE_Y_OFFSET-PADDLE_HEIGHT),PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setColor(Color.BLACK);
		paddle.setFillColor(Color.BLACK);
		paddle.setFilled(true);
		add(paddle);
		addMouseListeners();
		}

// makes paddle move
public void mouseMoved(MouseEvent e){
    paddle.setLocation(e.getX() - PADDLE_WIDTH/2, paddle.getY());
    if (paddle.getX() <= 0) paddle.setLocation(0, paddle.getY());
    if (paddle.getX() + PADDLE_WIDTH >= getWidth()) paddle.setLocation(getWidth() - PADDLE_WIDTH, paddle.getY());
}
private void playGame(){
		waitForClick();
		getBallVelocity();
		while (true){
			moveBall();
			if (ball.getY() >= getHeight()){
				break;
			}
			if (brickCounter == 0){
				break;
			}
		}
	}

	private void getBallVelocity(){
		vy = 4.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)){
			vx = -vx;
		}
	}
	
	private void moveBall(){
		ball.move(vx,  vy);
		
	//To check for collision.
		if ((ball.getX() - vx <= 0 && vx < 0) || (ball.getX() + vx >= (getWidth() - BALL_RADIUS * 2) && vx > 0)){
		vx = -vx;
		}
	
	if ((ball.getY() - vy <= 0 && vy < 0)){
		vy = -vy;
	}
	//Checking for other objects
	GObject collider = getCollidingObject();
	if (collider == paddle){
		if (ball.getY() >= getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * 2 && ball.getY() < getHeight() -PADDLE_Y_OFFSET - PADDLE_HEIGHT - BALL_RADIUS * 2 + 4){
			vy = -vy;
		}
	}
	else if (collider != null){
		remove(collider);
		brickCounter--;
		vy = -vy;
	}
	pause(DELAY);
}
	private GObject getCollidingObject(){
		if ((getElementAt(ball.getX(), ball.getY())) != null){
			return getElementAt(ball.getX(), ball.getY());
		}
		else if ((getElementAt((ball.getX() + BALL_RADIUS * 2), ball.getY()) != null)){
			return (getElementAt((ball.getX() + BALL_RADIUS * 2), ball.getY()));
		}
		else if ((getElementAt((ball.getX() + BALL_RADIUS * 2), (ball.getY() + BALL_RADIUS * 2)) != null)){
			return (getElementAt((ball.getX() + BALL_RADIUS * 2), ball.getY() + BALL_RADIUS * 2));
		}
		else if ((getElementAt((ball.getX()), (ball.getY() + BALL_RADIUS * 2)) != null)){
			return (getElementAt((ball.getX()), ball.getY() + BALL_RADIUS * 2));
		}
		else{
			return null;
		}
	}
	//This method display the Game Over message once the turn has elapse
/* */
	private void printGameOver(){
		GLabel label = new GLabel(" GAME OVER ", getWidth() / 2, getHeight() / 2);
		label.move(-label.getWidth()/2, -label.getHeight());
		label.setColor(Color.CYAN);
		add(label);
		
	}
	//this method display you win message once all the bricks had been broken
	/* */
	private void printWinner(){
		GLabel label = new GLabel(" YOU WON!!! ", getWidth() / 2, getHeight() / 2);
		label.move(-label.getWidth() / 2, label.getHeight());
		label.setColor(Color.RED);
		add(label);	
	}
}


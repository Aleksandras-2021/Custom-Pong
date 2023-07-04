import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {

	static final int GAME_WIDTH = 1000;
	static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.55555)); // 0.55555 == 5/9 ratio
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int PADDLE_WIDTH = 25;
	static final int PADDLE_HEIGHT = 100;
	static int BALL_AMOUNT = 1;
	static int EXTRA_BALL_AMOUNT = 2;
	ArrayList<Ball> balls = new ArrayList<Ball>();

	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Paddle paddle1;
	Paddle paddle2;
	// Ball ball;
	Score score;

	GamePanel() {
		newPaddles();
		newBall(BALL_AMOUNT);
		score = new Score(GAME_WIDTH, GAME_HEIGHT);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		this.setPreferredSize(SCREEN_SIZE);

		gameThread = new Thread(this);
		gameThread.start();

	}

	public void newBall(int amount) {
		for (int i = 0; i < amount; i++) {
			Ball ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), (GAME_HEIGHT / 2) - (BALL_DIAMETER / 2),
					BALL_DIAMETER, BALL_DIAMETER);
			balls.add(ball);
		}

	}

	public void newPaddles() {
		paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
		paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH,
				PADDLE_HEIGHT, 2);
	}

	public void paint(Graphics g) {
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0, 0, this);
	}

	public void draw(Graphics g) {
		paddle1.draw(g);
		paddle2.draw(g);
		for (Ball ball : balls) {
			ball.draw(g);
		}
		score.draw(g);
	}

	public void move() {
		paddle1.move();
		paddle2.move();
		for (Ball ball : balls) {
			ball.move();
		}
	}

	public void checkCollision() {
		for (Ball ball : balls) {
			// Ball bouncing off the roof and floor
			if (ball.y <= 0) {
				ball.setYDirection(-ball.yVelocity);
			}
			if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
				ball.setYDirection(-ball.yVelocity);
			}

			// Ball bouncing off paddles
			if (ball.intersects(paddle1)) {
				ball.xVelocity = Math.abs(ball.xVelocity);
				// Optional ****
				ball.xVelocity++;
				if (ball.yVelocity > 0) {
					ball.yVelocity++;
				} else {
					ball.yVelocity--;
				}
				ball.setXDirection(ball.xVelocity);
				ball.setYDirection(ball.yVelocity);
				// *************
			}
			if (ball.intersects(paddle2)) {
				ball.xVelocity = Math.abs(ball.xVelocity);
				// Optional ****
				ball.xVelocity++;
				if (ball.yVelocity > 0) {
					ball.yVelocity++;
				} else {
					ball.yVelocity--;
				}
				ball.setXDirection(-ball.xVelocity);
				ball.setYDirection(ball.yVelocity);
				// *************
			}
		}

		// Stops paddles at window edges
		if (paddle1.y <= 0) {
			paddle1.y = 0;
		}
		if (paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
			paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
		}

		if (paddle2.y <= 0) {
			paddle2.y = 0;
		}
		if (paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) {
			paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
		}

		// Assigns scores & creates new paddles & balls
		boolean resetGame = false;

		for (Ball ball : balls) {
			if (ball.x <= 0) {
				score.player2++;
				balls.remove(ball);
				if (balls.isEmpty()) {
					resetGame = true;
				}
				break;
			}
			if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
				score.player1++;
				balls.remove(ball);
				if (balls.isEmpty()) {
					resetGame = true;
				}
				break;

			}
		}
		if (resetGame) {
			// balls.removeAll(balls);
			newPaddles();

			if ((Score.player1 % 5 == 0 && Score.player1 /5 >=1) || (Score.player2 % 5 == 0 && Score.player2 / 5 >= 1))
				newBall(EXTRA_BALL_AMOUNT);
			else
				newBall(BALL_AMOUNT);
		}
	}

	public void run() {
		// game loop
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while (true) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}

	public class AL extends KeyAdapter {// AL == Action Listener

		public void keyPressed(KeyEvent e) {
			paddle1.keyPressed(e);
			paddle2.keyPressed(e);
		}

		public void keyReleased(KeyEvent e) {
			paddle1.keyReleased(e);
			paddle2.keyReleased(e);
		}
	}

}

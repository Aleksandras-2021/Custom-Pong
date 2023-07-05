import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class PowerUp extends Rectangle{

	
	Random random;
	int xVelocity;
	int yVelocity;
	int initialSpeed = 2;
	int x,y;

	PowerUp(int x, int y,int width, int height) {
		super(x, y,width,height);
		this.x = x;
		this.y = y;
		random = new Random();
	}

	public void draw(Graphics g) {
		g.setColor(Color.CYAN);
		g.fillOval(x, y,width,height);
	}
	
	
	
}

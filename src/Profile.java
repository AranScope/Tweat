import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Random;

//import twitter4j.*;

public class Profile {
	
		private Vector2 pos;
		private float size;
		private float vel;
		private Profile target;
		private Vector2 targetVec;
		private boolean alive;
		private String name;
		private float radius;
		private Random myRandom;
		private int startScore;
		private int endScore;
		
		public Profile(float size, String name) {
			this.size =size;
			alive = true;
			vel = 1F/size;
			calcRadius();
			pos = Vector2.getRandomVector((Board.MAX_WIDTH-2*radius), (Board.MAX_HEIGHT-2*radius));
			pos.setX(pos.getX()+radius);
			pos.setY(pos.getY()+radius);
			targetVec = Vector2.getRandomVector(Board.MAX_WIDTH, Board.MAX_HEIGHT);
			myRandom = new Random();
			this.name = name;
			startScore = (int)size*200;
			System.out.println(pos);
						
		}
		
		public void update() {
			calcRadius();
			move();
			checkCollision();
			if (myRandom.nextInt(10000) > 9995) decay();
			if (size < 0) {
				alive = false;
			}
			if (size < 1) {
				size = 1;
			}
		}
		
		public void decay() {
			this.size -= (float) (Math.log(size)/25);
		}
		
		private void move() {
			Vector2 temp = pos.add(pos.vectorTowards(targetVec).normalise().mult(vel));
			/*if (pos.add(temp).getX() < (0+radius) || pos.add(temp).getX() > (Board.MAX_WIDTH -radius)) {
				targetVec.setX(-targetVec.getX());
				System.out.println(pos);
			}
			if (pos.add(temp).getY() < (0+radius) || pos.add(temp).getY() > (Board.MAX_HEIGHT -radius)) {
				targetVec.setY(-targetVec.getY());
				System.out.println(pos);

			}*/
			pos = pos.add(pos.vectorTowards(targetVec).normalise().mult(vel));
			targetVec = targetVec.add(pos.vectorTowards(targetVec).normalise().mult(vel));
		}
		
		private void checkCollision() {
			for (Profile p: Board.players) {
				if (p.getSize() < size && p.getSize() > 0) {
					if (pos.getDistanceTo(p.getVector()) < radius/2) {
						this.size += 4*Math.log(p.getSize()+1)/(size);
						p.setSize(-1);
						System.out.println(size);
						
					}
				}
			}
		}
		
		public void newTarget(Profile target) {
			this.target = target;
			targetVec = target.getVector();
		}
		
		public Vector2 getVector() {
			return pos;
		}
		
		public void setVector(Vector2 newPos) {
			this.pos = newPos;
		}
		
		
		public void draw(Graphics g) {	
			g.setColor(Color.white);
			g.fillOval((int)(pos.getX()-radius/2), (int)(pos.getY()-radius/2), (int)(radius),(int)(radius));
			g.setColor(Color.black);
			g.drawOval((int)(pos.getX()-radius/2), (int)(pos.getY()-radius/2), (int)(radius),(int)(radius));
			Font small = new Font("Helvetica", Font.BOLD, 14);
			g.setFont(small);
			FontMetrics metr;
    		metr = g.getFontMetrics(small);
    		g.drawString(name, (int)(pos.getX())-metr.stringWidth(name)/2, (int)(pos.getY()+metr.getHeight()/3));
			
			
		}
		
		private void calcRadius() {
			this.radius = (float) (40*size*Math.log(size)/3+50)/2;
		}
		
		public float getRadius() {
			return radius;
		}
		
		public float getSize() {
			return size;
		}
		
		public void setSize(float size) {
			this.size = size;
		}
		public boolean isAlive() {
			return alive;
		}	
}

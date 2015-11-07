import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

//import twitter4j.*;

public class Profile {
	
		private Vector2 pos;
		private float size;
		private float vel;
		private Profile target;
		private Vector2 targetVec;
		private boolean alive;
		private String name;
		
		public Profile(float size, String name) {
			this.size =size;
			alive = true;
			vel = 5f/size;
			pos = Vector2.getRandomVector(Board.MAX_WIDTH, Board.MAX_HEIGHT);
			targetVec = Vector2.getRandomVector(Board.MAX_WIDTH, Board.MAX_HEIGHT);
			System.out.println();
			this.name = name;
						
		}
		
		public void update() {
			move();
			checkCollision();
			if (size < 0) {
				alive = false;
			}			
		}
		
		private void move() {	
			pos = pos.add(pos.vectorTowards(targetVec).normalise().mult(vel));
			targetVec = targetVec.add(pos.vectorTowards(targetVec).normalise().mult(vel));
			//pos = pos.add(new Vector2(1,1));
		}
		
		private void checkCollision() {
			for (Profile p: Board.players) {
				if (p.getSize() < size && p.getSize() > 0) {
					if (pos.getDistanceTo(p.getVector()) < size/2) {
						this.size += 4*Math.log(p.getSize()+1)/(size*0.1f);
						p.setSize(-1);
						
					}
				}
			}
		}
		
		public void newTarget(Profile target) {
			this.target = target;
			targetVec = target.getVector();
			System.out.println(targetVec);
		}
		
		public Vector2 getVector() {
			return pos;
		}
		
		public void setVector(Vector2 newPos) {
			this.pos = newPos;
		}
		
		
		public void draw(Graphics g) {	
			g.setColor(Color.white);
			g.fillOval((int)(pos.getX()-size/2), (int)(pos.getY()-size/2), (int)(size),(int)(size));
			g.setColor(Color.black);
			g.drawOval((int)(pos.getX()-size/2), (int)(pos.getY()-size/2), (int)(size),(int)(size));
			Font small = new Font("Helvetica", Font.BOLD, 14);
			g.setFont(small);
			FontMetrics metr;
    		metr = g.getFontMetrics(small);
    		g.drawString(name, (int)(pos.getX())-metr.stringWidth(name)/2, (int)(pos.getY()+metr.getHeight()/3));
			
			
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

import java.awt.Graphics;

//import twitter4j.*;

public class Profile {
	
		private Vector2 pos;
		private float size;
		private float vel;
		private Profile target;
		private Vector2 targetVec;
		private boolean alive;
		
		public Profile(float size) {
			this.size =size;
			alive = true;
			vel = 5f/size;
			pos = new Vector2(50,50);
			targetVec = Vector2.getRandomVector(Board.MAX_WIDTH, Board.MAX_HEIGHT);
			System.out.println();
						
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
			//pos = pos.add(new Vector2(1,1));
		}
		
		private void checkCollision() {
			
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
			g.drawOval((int)(pos.getX()+size/2), (int)(pos.getY()+size/2), (int)(size),(int)(size));
		}
		
		public boolean getAlive() {
			return alive;
		}	
}

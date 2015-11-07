//import twitter4j.*;

public class Profile {
	
		private Vector2 pos;
		private float size;
		private float vel;
		private Profile target;
		private boolean alive;
		
		public void Profile(float size) {
			this.size =size;
			alive = true;
			vel = 2;
		}
		
		public void update() {
			move();
			checkCollision();
			if (size < 0) {
				alive = false;
			}
		}
		
		private void move() {	
			if (target != null) {
				pos = pos.vectorTowards(target.getVector().normalise().mult(2));
			}
		}
		
		private void checkCollision() {
			
		}
		
		public void newTarget(Profile target) {
			this.target = target;
		}
		
		public Vector2 getVector() {
			return pos;
		}
		
		public void setVector(Vector2 newPos) {
			this.pos = newPos;
		}
		
		
}


public class Profile {
	
		private int x;
		private int y;
		private int size;
		private int speed;
		private int targetX;
		private int targetY;
		boolean alive;
		
		public void Profile(int size) {
			this.size =size;
			this.speed = 10;
			alive = true;
		}
		
		public void updatePerson() {
			move();
			checkCollision();
			if (size < 0) {
				alive = false;
			}
		}
		
		private void move() {
						
		}
		
		private void checkCollision() {
			
		}
		
		
}

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import twitter4j.User;

public class Profile {
	
		private Vector2 pos;
		private float size;
		private float vel;
		private Profile target;
		private Vector2 targetVec;
		private boolean alive;
		private String name;
		private float radius;
		public Random myRandom;
		private int startScore;
		private int endScore;
		private User user;
		private BufferedImage image;
	public boolean isFollowed;
		
		public Profile(User user) {
			this.user = user;
			image = TwitterW.getProfileImage(user);
			size = TwitterW.getSize(user);
			if(size < 1) size = 1;
			name = "@"+user.getScreenName();
			alive = true;
			vel = 0.5F/size;
			calcRadius();
			pos = Vector2.getRandomVector((Board.MAX_WIDTH-2*radius), (Board.MAX_HEIGHT-2*radius));


			pos.setX(pos.getX()+radius);
			pos.setY(pos.getY()+radius);
			targetVec = Vector2.getRandomVector(Board.MAX_WIDTH, Board.MAX_HEIGHT);
			myRandom = new Random();
			startScore = (int)size*200;

		}

	public User getUser() {
		return user;
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
				setSize(1);
			}
		}
		
		public void decay() {
			float tempSize = size -(float) (Math.log(size)/20);
			setSize(size);
		}
		
		private void move() {
			pos = pos.add(pos.vectorTowards(targetVec).normalise().mult(vel));
			targetVec = targetVec.add(pos.vectorTowards(targetVec).normalise().mult(vel));
			
			//Arans sketchy code
			if(pos.getX() > Board.MAX_WIDTH - radius && targetVec.getX() > 0){
				targetVec = targetVec.mult(new Vector2(-1, 1));
			}
			if(pos.getX() < 0 + radius && targetVec.getX() < 0){
				targetVec = targetVec.mult(new Vector2(-1, 1));
			}
			if(pos.getY() > Board.MAX_HEIGHT - radius && targetVec.getY() > 0){
				targetVec = targetVec.mult(new Vector2(1, -1));
			}
			if(pos.getY() < 0 + radius && targetVec.getY() < 0){
				targetVec = targetVec.mult(new Vector2(1, -1));
			}
			
		}
		
		private void checkCollision() {
			for (Profile p: Board.players) {
				if (p.getSize() < size && p.getSize() > 0) {
					if (pos.getDistanceTo(p.getVector()) < radius) {
						setSize((float) (size+ 4*Math.log(p.getSize()+1)/(size)));
						p.setSize(-1);						
					}
				}
			}
		}
		
		public boolean intersects(Profile check){
			if(pos.getDistanceTo(check.getVector()) < radius + check.getRadius() + 20) return true;
			else return false;
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

		public void draw(Graphics2D g) {
			int x = (int)(pos.getX()-radius);
			int y = (int)(pos.getY()-radius);
			int paintSize = (int)(radius*2);

			g.setClip(new Ellipse2D.Double(x, y, paintSize, paintSize));
			g.drawImage(image, x, y,paintSize, paintSize, null);

			g.setClip(null);

			g.setColor(Color.white);
			g.setStroke(new BasicStroke(2, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT));
			g.drawOval(x, y,paintSize, paintSize);

			g.setFont(new Font("Seruf", Font.PLAIN, 20));
			FontMetrics metr;
			metr = g.getFontMetrics();
			g.drawString(name, (int)(pos.getX())-metr.stringWidth(name)/2, (int)(pos.getY() + radius + metr.getHeight() + 8));

		}
		
		private void calcRadius() {
			this.radius = (float) (Board.scale*40*size*Math.log(size)/6+50)/2;
		}
		
		public float getRadius() {
			return radius;
		}
		
		public float getSize() {
			return size;
		}
		
		public void setSize(float size) {
			this.size = size;
			this.vel = 0.5F/size;
		}
		public boolean isAlive() {
			return alive;
		}	
		
		public String getName() {
			return name;
		}
}

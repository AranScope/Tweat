import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;

import twitter4j.TwitterException;
import twitter4j.User;

import javax.imageio.ImageIO;

public class Profile {
		private Vector2 pos;
		private float size;
		private float vel;
		private Vector2 targetVec;
		private String name;
		private float radius;
		private Random myRandom;
		private User user;
		private BufferedImage image;
		private String tweet = "";
		private BufferedImage twitterlogo;
		public boolean isFollowed; //sam's variable.

		private long startTime = 0;

	TwitterW twitterW = Board.twitterW;

	public Vector2 getTargetVec(){
		return this.targetVec;
	}

	public String[] deathMsgs = {
			"%s got #rekt! #TwEAT",
			"Looks like %s got violated #TwEAT",
			"%s has been #dispatched! #TwEAT",
			"%s looks like someone was hungry #TwEAT",
			"%s is now sleeping with the fishes #TwEAT",
			"%s got tyrannosaurus #rekt #TwEAT",
			"Someone went cannibal on %s #TwEAT"
	};
		
		public Profile(User user) {
			try {
				twitterlogo = ImageIO.read(new File("res/twitterlogo.png"));
			}catch(IOException ex){ex.printStackTrace();}

			this.user = user;
			image = twitterW.getProfileImage(user);
			size = twitterW.getSize(user);
			name = "@"+user.getScreenName();
			vel = 1.25f/size;
			calcRadius();
			pos = Vector2.getRandomVector((Board.MAX_WIDTH-2*radius), (Board.MAX_HEIGHT-2*radius));

			pos.setX(pos.getX()+radius);
			pos.setY(pos.getY()+radius);
			targetVec = Vector2.getRandomVector(Board.MAX_WIDTH, Board.MAX_HEIGHT);
			myRandom = new Random();

		}

		public Random getRand() {
		return myRandom;
	}


		public void update() {
			if(startTime != -1) {
				if (System.currentTimeMillis() - startTime > 5000) {
					tweet = "";
					startTime = -1;
				}
			}

//			if(size > 10)

			calcRadius();
			move();
			checkCollision();
			//if (myRandom.nextInt(10000) > 9950) decay();
			decay();
			if (size < 1) {
				setSize(1);
			}
		}
		
		public void decay() {
			//float tempSize = size -(float) (Math.log(size)/10);
			//setSize(tempSize);

			float tempSize = size - (float) (Math.log(size)/3000);
			setSize(tempSize);
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
				if(p != this) {
					if (p.getSize() < size && p.getSize() > 0) {
						if (pos.getDistanceTo(p.getVector()) < radius) {
							setSize((float) (size + 4 * Math.log(p.getSize() + 1) / (size)));
							p.reset();
						}
					}
				}
			}
		}
		
		public boolean intersects(Profile check){
			if(pos.getDistanceTo(check.getVector()) < radius + check.getRadius() + 20) return true;
			else return false;
		}
		
		public void newTarget(Profile target) {
			this.targetVec = target.getVector();
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
			g.drawString(name, (int)(pos.getX())-metr.stringWidth(name)/2, (int)(pos.getY() + radius + metr.getHeight() + 4));

			if(!tweet.equals("")) {
				g.setColor(Color.white);

				int xOffset = (int) (pos.getX() + radius);
				int yOffset = (int) (pos.getY() - radius);

				g.setFont(new Font("Seruf", Font.PLAIN, 15));

				FontMetrics fm = g.getFontMetrics();

				g.fillRoundRect(xOffset, yOffset, fm.stringWidth(tweet) + 20 + 32, 40, 25, 25);

				int[] xpoints = {15 + xOffset, 30 + xOffset, 15 + xOffset};
				int[] ypoints = {40 + yOffset, 40 + yOffset, 55 + yOffset};
				g.fillPolygon(xpoints, ypoints, 3);

				g.setColor(Color.decode("0x4099FF"));

				g.drawString(tweet, 42 + xOffset, 25 + yOffset);

				g.drawImage(twitterlogo, 12 + xOffset, 11 + yOffset, twitterlogo.getWidth() / 50, twitterlogo.getHeight() / 50, null);
			}
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
			this.vel = 1.25F/size;
		}
		
		public String getName() {
			return name;
		}
		
		public User getUser() {
			return user;
		}
		
		public void setTweet(String tweet) {
			if(tweet.length() > 28) tweet = tweet.substring(0, 28) + "...";
			this.tweet = tweet;
			startTime = System.currentTimeMillis();
		}

		public void reset() {
			this.setVector(Vector2.getRandomVector((Board.MAX_WIDTH - 2 * this.getRadius()), (Board.MAX_HEIGHT - 2 * this.getRadius())));
			this.setSize(twitterW.getSize(this.getUser()));
			this.tweet = "";
		}

//		try {
//			int msg = this.getRand().nextInt(deathMsgs.length);
//			twitterW.tweet(String.format(deathMsgs[msg], this.getName()));
//		} catch(TwitterException e) {
//			e.printStackTrace();
//		}
	}


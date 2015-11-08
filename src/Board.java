import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.User;

public class Board extends JPanel implements ActionListener {
	private Timer timer;
	public static int B_WIDTH = 1400;
	public static int B_HEIGHT = 800;
	public static int MAX_WIDTH = 1400;
	public static int MAX_HEIGHT = 800;
	public static final int DELAY = 1;
	public static float scale;

    private BufferedImage vignette;

    private Profile[] leaderBoard;
    private ArrayList<Status> tweets = new ArrayList<>();

    private DecimalFormat df = new DecimalFormat("#0.00");
	
	public static ArrayList<Profile> players;
	private static ArrayList<User> followers;
    
    public Board() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	B_WIDTH = (int)screenSize.getWidth();
    	B_HEIGHT = (int)screenSize.getHeight() - 50;
    	MAX_WIDTH = B_WIDTH;
    	MAX_HEIGHT = B_HEIGHT;
    	initGame();
        setBackground(Color.decode("0X55ACED"));
        setFocusable(true);
        setDoubleBuffered(false);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
    }
    
    private void initGame() {
        try {
            vignette = ImageIO.read(new File("res/vignette.png"));
        }catch(IOException ex){ex.printStackTrace();}

    	followers = new ArrayList<>();
    	try{
    		followers = TwitterW.getFollowers(TwitterW.getUser("tweatgame"));  		
        }catch(Exception e){}
    	timer = new Timer(DELAY, this);
         
        players = new ArrayList<>();
        
        for (User u: followers) {
        	Profile profile = new Profile(u);
        	boolean intersects = true;
        	
        	if(players.size()== 0) players.add(profile);
        	else while(intersects){
        		profile.setVector(Vector2.getRandomVector((MAX_WIDTH - 2 * profile.getRadius()), (MAX_HEIGHT-2*profile.getRadius())));
        		for(int x = 0; x < players.size(); x++){
        			if(players.get(x) != profile){
        				intersects = profile.intersects(players.get(x));
        				if(intersects) break;
        			}
        		}
        	}
        	
        	if(players.size() > 0) players.add(profile);
        	scale = (float) (15.0/players.size());
        }

        leaderBoard = getLeaderboard();

        try {
            TwitterW.onTweet(new StatusListener() {
            	String pattern = "@(\\w+)";
            	Pattern r = Pattern.compile(pattern);
            	
                @Override
                public void onStatus(Status status) {
                    if(tweets.size() > 4){
                        tweets.remove(0);
                    }

                    tweets.add(status);

                    Matcher m = r.matcher(status.getText());
                    String userName = "";
                    
                    if (m.find( )) {
            	    	userName = m.group(1).toLowerCase();
            	    }
                    
                    top:for (Profile p: players) {
                    	if (p.getName().equalsIgnoreCase("@"+status.getUser().getScreenName())) {	
                    		p.setTweet(status.getText());

                    		p.setSize(p.getSize()+4/p.getSize());
                    		for (Profile q: players) {
                    			if (q.getName().equalsIgnoreCase("@"+userName)) {
                    				p.newTarget(q);
                    				break top;
                    			}
                    		}
                    	}
                    }
                }

                @Override
                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

                }

                @Override
                public void onTrackLimitationNotice(int i) {

                }

                @Override
                public void onScrubGeo(long l, long l1) {

                }

                @Override
                public void onStallWarning(StallWarning stallWarning) {

                }

                @Override
                public void onException(Exception e) {

                }
            });
    		int i = 0;
            User[] us = new User[followers.size()];
            for(User user: followers){
                us[i++] = user;
            }
            TwitterW.listen(us);
        } catch (Exception e) {
        	e.printStackTrace();
        }

        timer.start();
    }

    public Profile[] getLeaderboard() {
        int size = Math.min(5, players.size());

        Profile[] leaderboard = new Profile[size];
        Collections.sort(players, new Comparator<Profile>() {
            @Override
            public int compare(Profile o1, Profile o2) {
                boolean less = o1.getSize() < o2.getSize();
                boolean eq = o1.getSize() == o2.getSize();
                return less ? 1 : (eq ? 0 : -1);
            }
        });
        int i = 0;
        for(; i < size; i++) leaderboard[i] = players.get(i);
        return leaderboard;
    }
   
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGame(g);
    }

    private void paintGame(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(vignette != null){
            g2.drawImage(vignette, 0, 0, getWidth(), getHeight(), null);
        }

    	for (Profile p: players) {
            p.draw(g2);
    	}

        paintLeaderboard(g2);
        paintFeed(g2);
    }

    private void paintLeaderboard(Graphics2D g){
        int xOffset = getWidth() - 210;
        int yOffset = 35;
        int height = 40;
        int width = 200;
        int margin = 5;

        g.setFont(new Font("Seruf", Font.PLAIN, 15));

        g.setColor(Color.white);
        g.drawString("Leaderboard", 10 + xOffset, yOffset - 10);


        for(int count = 0; count < leaderBoard.length; count++){
            g.setColor(new Color(255, 255, 255, 200));
            g.fillRoundRect(xOffset, yOffset + (count * (height + margin)) , width, height, 15, 15);

            g.setColor(Color.decode("0x4099FF"));
            g.drawString(leaderBoard[count].getName(), 10 + xOffset, 25 + yOffset + (count * (height + margin)));

            g.drawString("" + df.format(leaderBoard[count].getSize()), 10 + xOffset + width - 60, 25 + yOffset + (count * (height + margin)));
        }
    }

    private void paintFeed(Graphics2D g){
        int xOffset = getWidth() - 210;
        int yOffset = 300;
        int height = 70;
        int width = 200;
        int margin = 5;

        if(tweets.size() > 0) {
            g.setFont(new Font("Seruf", Font.PLAIN, 15));
            g.setColor(Color.white);
            g.drawString("Twitter Feed", 10 + xOffset, yOffset - 10);
        }

        int posCount = 0;

        for(int count = tweets.size() - 1; count >= 0; count--){
            if(tweets.get(count) != null) {
                g.setColor(new Color(255, 255, 255, 200));
                g.fillRoundRect(xOffset, yOffset + (posCount * (height + margin)), width, height, 15, 15);

                g.setColor(Color.decode("0x4099FF"));
                g.drawString("@" + tweets.get(count).getUser().getScreenName(), 10 + xOffset, 25 + yOffset + (posCount * (height + margin)));
                drawParagraph(g, tweets.get(count).getText(), 180, 10 + xOffset, 35 + yOffset + (posCount * (height + margin)));
                posCount++;
            }
        }


    }

    void drawParagraph(Graphics2D g, String paragraph, float width, float x, float y) {
        LineBreakMeasurer linebreaker = new LineBreakMeasurer(new AttributedString(paragraph)
                .getIterator(), g.getFontRenderContext());

        int count = 0;
        while (linebreaker.getPosition() < paragraph.length()) {
            TextLayout tl = linebreaker.nextLayout(width);
            y += tl.getAscent();
            tl.draw(g, x, y);
            y += tl.getDescent() + tl.getLeading();
            count += 1;
            if(count == 2) break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        leaderBoard = getLeaderboard();

    	for (Profile p: players) {
    		p.update();
    	}
    	repaint();
    }
}
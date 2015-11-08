

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
	public static ArrayList<Profile> players;
	private static ArrayList<Profile> toRemove;
	private static ArrayList<User> followers;
    
    public Board() {
    	
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	B_WIDTH = (int)screenSize.getWidth();
    	B_HEIGHT = (int)screenSize.getHeight() - 50;
    	MAX_WIDTH = B_WIDTH;
    	MAX_HEIGHT = B_HEIGHT;
    	initGame();
        addKeyListener(new TAdapter());
        setBackground(Color.decode("0X55ACED"));
        setFocusable(true);
        setDoubleBuffered(false);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));  
        
    }
    
    public void addPlayer(User user) {
    	players.add(new Profile(user));
    }
    
    private void initGame() {
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
        		profile.setVector(Vector2.getRandomVector((MAX_WIDTH-2*profile.getRadius()), (MAX_HEIGHT-2*profile.getRadius())));
        		for(int x = 0; x < players.size(); x++){
        			if(players.get(x) != profile){
        				intersects = profile.intersects(players.get(x));
        				if(intersects) break;
        			}
        		}
        	}
        	
        	if(players.size() > 0) players.add(profile);
        }     
       
        
        toRemove = new ArrayList<>();
        try {
            TwitterW.onTweet(new StatusListener() {
            	String pattern = "@(\\w+)";
            	Pattern r = Pattern.compile(pattern);
            	
                @Override
                public void onStatus(Status status) {
                    System.out.println(status.getUser().getScreenName() + ", " + status.getText());
                    Matcher m = r.matcher(status.getText());
                    String userName = "";
                    
                    if (m.find( )) {
            	    	userName = m.group(1).toLowerCase();
            	    }
                    
                    System.out.println(userName);
                    top:for (Profile p: players) {
                    	if (p.getName().equalsIgnoreCase("@"+status.getUser().getScreenName())) {	
                    		System.out.println("we found you: " + p.getName());
                    		p.setSize(p.getSize()+4/p.getSize());
                    		for (Profile q: players) {
                    			if (q.getName().equalsIgnoreCase("@"+userName)) {
                    				p.newTarget(q);
                    				System.out.println("TEST "+q.getName());
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
                System.out.println(user.getScreenName());
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
        players.sort(new Comparator<Profile>() {
            @Override
            public int compare(Profile o1, Profile o2) {
                boolean less = o1.getSize() < o2.getSize();
                boolean eq = o1.getSize() == o2.getSize();
                return less ? -1 : (eq ? 0 : 1);
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

    	for (int i  = 0; i < players.size(); i++) {
            Profile p = players.get(i);
    		if (p.isAlive()) p.draw(g2);
    		else {
    			players.remove(i);
                System.out.println("Removing");
                try {
                    TwitterW.tweet("@" + p.getName() + " you died m8 #shrekt");
                } catch(TwitterException e) {
                    e.printStackTrace();
                }
                i--;
    		}
    	}
    }
    
    private void paintMenu(Graphics g) {
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	for (Profile p: players) {
    		p.update();
    	}
    	repaint();
    }

	
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

        }	
    }
}


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
import java.util.Collections;
import java.util.Comparator;
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
	public static float scale;

    public String[] deathMsgs = {
            "You got #rekt by @%s! #TwEAT",
            "Looks like @%s violated you #TwEAT",
            "You've been #dispatched by @%s! #TwEAT",
            "You've been drowned in @%s #TwEAT",
            "Looks like @%s was hungry #TwEAT",
            "You are now one with @%s #TwEAT"
    };
	
	public static ArrayList<Profile> players;
	private static ArrayList<Profile> toRevive;
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
        	scale = (float) (15.0/players.size());
        }     
       
        
        toRevive = new ArrayList<>();
        try {
            TwitterW.onTweet(new StatusListener() {
            	String pattern = "@(\\w+)";
            	Pattern r = Pattern.compile(pattern);
            	
                @Override
                public void onStatus(Status status) {
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
		
    	
    	for (Profile p: players) {
    		if (p.isAlive()) p.draw(g2);
    		else {
    			toRevive.add(p);
    		}
    	}
    	for (Profile p: toRevive) {
    		players.remove(p);
    		try {
                int msg = p.getRand().nextInt(deathMsgs.length + 1);
                TwitterW.tweet(deathMsgs[msg], p.getName());
            } catch(TwitterException e) {
                e.printStackTrace();
            }
    		players.add(new Profile(p.getUser()));
    		
    	}
    	toRevive = new ArrayList<>();
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
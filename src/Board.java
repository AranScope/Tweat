

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import twitter4j.TwitterException;

public class Board extends JPanel implements ActionListener {

	private Timer timer;
	public static int B_WIDTH = 1400;
	public static int B_HEIGHT = 800;
	public static final int MAX_WIDTH = 1400;
	public static final int MAX_HEIGHT = 800;
	public static final int DELAY = 2;
	
	public static ArrayList<Profile> players;
	private static ArrayList<Profile> toRemove;
    
    public Board() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	initGame();
    	B_WIDTH = (int)screenSize.getWidth();
    	B_HEIGHT = (int)screenSize.getHeight()-100;
        addKeyListener(new TAdapter());
        setBackground(Color.decode("0X55ACED"));
        setFocusable(true);
        setDoubleBuffered(false);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));  
        
    }
    
    public void addPlayer(int size, String name) {
    	players.add(new Profile(size, name));
    }
    
    private void initGame() {	
    	timer = new Timer(DELAY, this);
        timer.start(); 
        players = new ArrayList<>();
        addPlayer(5, "Gary");
        addPlayer(10, "Fat Sam");
        players.get(0).newTarget(players.get(1));
        players.get(1).newTarget(players.get(0));
        toRemove = new ArrayList<>();
        /*try {
			TwitterW.tweet("@PeranTruscott #gamestarting");
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }

   
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGame(g);
    }
   
    
    private void paintGame(Graphics g) {
    	for (Profile p: players) {
    		if (p.isAlive()) p.draw(g);
    		else {
    			toRemove.add(p);
    		}
    	}
    	for (Profile p: toRemove) {
    		players.remove(p);
    		System.out.println("Removing");
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
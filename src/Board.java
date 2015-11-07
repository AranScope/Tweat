

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	public static int B_WIDTH = 1400;
	public static int B_HEIGHT = 800;
	boolean finalLevel = false;
	public ArrayList<Profile> players;
    
    public void Board() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	B_WIDTH = (int)screenSize.getWidth();
    	B_HEIGHT = (int)screenSize.getHeight()-100;
        addKeyListener(new TAdapter());
        setBackground(Color.decode("0X55ACED"));
        setFocusable(true);
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));        
        
    }
    
    public void addPlayer(int size) {
    	
    }
    
    private void initGame() {	

    }

   
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
   
    
    private void paintGame(Graphics g) {
  
    }
    
    private void paintMenu(Graphics g) {
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }

	
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

        }	
    }
}


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
	private Random rand;
	int roadCounter;
	private int DELAY;
	private Image frogImg;
	private ArrayList<String> dates;
	private int dateNum;
	private Timer timer;
	private String currentDate;
	private int dateCounter;
	private int fileNum;
	boolean finalLevel = false;
    
    public Board() {
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        addKeyListener(new TAdapter());
        setBackground(Color.decode("0X55ACED"));
        setFocusable(true);
        setDoubleBuffered(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));        
        
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
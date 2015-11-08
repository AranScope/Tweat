import java.awt.EventQueue;

import javax.swing.JFrame;


public class Application extends JFrame{
	public Board board;
	
    public Application() {
        add(board = new Board());    
        setResizable(false);
        pack();
        setTitle("TwEAT");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    

    public static void main(String[] args) {
    	System.out.println("Starting");
        final Application ex = new Application();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	ex.setVisible(true);   
            }
        });
    }
}

import javax.swing.*;

/**
 * Created by aranscope on 08/11/15.
 */
public class AranTestClass2 {
    public static void main(String[] args){
        JFrame frame = new JFrame("Twat");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new AranTestView());
        frame.pack();
    }
}

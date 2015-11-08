import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by aranscope on 07/11/15.
 */
public class AranTestView extends JPanel {
    String msg = "";
    BufferedImage twitterlogo;
    public AranTestView(){
        try {
            twitterlogo = ImageIO.read(new File("res/twitterlogo.png"));
        }catch(IOException ex){ex.printStackTrace();}

        this.setPreferredSize(new Dimension(800, 600));

        msg = "I'm coming for you @SamTebbs what are you going to do";
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.white);
    }
}

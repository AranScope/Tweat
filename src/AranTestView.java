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
//        Graphics2D g2 = (Graphics2D) g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//
//        g2.setColor(Color.white);
//
//        int xOffset = pos.getX();
//        int yOffset = pos.getY();
//
//        g2.setFont(new Font("Seruf", Font.PLAIN, 15));
//
//        FontMetrics fm = g2.getFontMetrics();
//
//        g2.fillRoundRect(xOffset, yOffset, fm.stringWidth(msg) + 20 + 32, 40, 25, 25);
//
//        int[] xpoints = {15 + xOffset, 30 + xOffset, 15 + xOffset};
//        int[] ypoints = {40 + yOffset, 40 + yOffset, 55 + yOffset};
//        g2.fillPolygon(xpoints, ypoints, 3);
//
//        g2.setColor(Color.decode("0x4099FF"));
//
//        g2.drawString(msg, 42 + xOffset, 25 + yOffset);
//
//        g2.drawImage(twitterlogo, 12 + xOffset, 11 + yOffset, twitterlogo.getWidth()/50, twitterlogo.getHeight()/50, null);


    }


}

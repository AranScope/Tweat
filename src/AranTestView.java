import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by aranscope on 07/11/15.
 */
public class AranTestView extends JPanel {
    BufferedImage profileImage;
    public AranTestView(BufferedImage profileImage){
        this.setPreferredSize(new Dimension(800, 600));
        this.profileImage = profileImage;
    }

    public void paintComponent(Graphics g){
        g.drawImage(profileImage, 0, 0, 100, 100, null);
    }


}

import twitter4j.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by aranscope on 07/11/15.
 */
public class AranTestClass extends JPanel{
    public static void main(String[] args){
        JFrame frame = new JFrame("Tweat");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try{
            ArrayList<User> followers = TwitterW.getFollowers(TwitterW.getUser("tweatgame"));

            for(User user: followers){
                System.out.println(user.getScreenName());
            }
        }catch(Exception e){}

        try {

            User user = TwitterW.getUser("aranscope");
            User user1 = TwitterW.getUser("vivadaylight3");

            TwitterW.onTweet(new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    System.out.println(status.getUser().getScreenName() + ", " + status.getText());
                    if(status.getText().toLowerCase().contains("@tweatgame")){

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

            TwitterW.listen(user);
            TwitterW.listen(user1);

            //frame.add(new AranTestView(TwitterW.getProfileImage(TwitterW.getUser("aranscope"))));



        }catch(Exception e){e.printStackTrace();}

        frame.pack();
    }
}
